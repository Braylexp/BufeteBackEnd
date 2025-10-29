package com.bufete.backend.service;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bufete.backend.model.Categoria;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.base-path:bufete-archivos}")
    private String basePath;

    public String uploadFilePlantilla(MultipartFile file, Categoria.TipoCategoria modulo, String nameCategory) throws IOException {
        
        String key = generateStorageKeyForPlantilla(modulo, nameCategory, file.getOriginalFilename());
        
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .metadata(Map.of(
                        "original-name", file.getOriginalFilename(),
                        "categoria-name", nameCategory,
                        "modulo-name", modulo.toString()
                    ))
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            
            log.info("Archivo subido exitosamente: {}", key);
            return key;
            
        } catch (Exception e) {
            log.error("Error subiendo archivo a S3: {}", e.getMessage());
            throw new RuntimeException("Error subiendo archivo a S3", e);
        }
    }

    public String uploadFileSentencia(MultipartFile file, String modulo) throws IOException {
        
        String key = generateStorageKeyForSentencia(modulo, file.getOriginalFilename());
        
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .metadata(Map.of(
                        "original-name", file.getOriginalFilename(),
                        "modulo-name", modulo.toString()
                    ))
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            
            log.info("Archivo subido exitosamente: {}", key);
            return key;
            
        } catch (Exception e) {
            log.error("Error subiendo archivo a S3: {}", e.getMessage());
            throw new RuntimeException("Error subiendo archivo a S3", e);
        }
    }

    public String uploadFileToExpediente(MultipartFile file, Long expedienteId, UUID nodeId, Long abogadoId) throws IOException {
        
        String key = generateStorageKey(expedienteId, nodeId, file.getOriginalFilename(), abogadoId);
        
        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .metadata(Map.of(
                        "original-name", file.getOriginalFilename(),
                        "expediente-id", expedienteId.toString(),
                        "node-id", nodeId.toString()
                    ))
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            
            log.info("Archivo subido exitosamente: {}", key);
            return key;
            
        } catch (Exception e) {
            log.error("Error subiendo archivo a S3: {}", e.getMessage());
            throw new RuntimeException("Error subiendo archivo a S3", e);
        }
    }

    public String generatePresignedDownloadUrl(String storageKey, Duration duration, String name) {
        try {
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .responseContentDisposition("attachment; filename=\"" + name + "\"")
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(duration)
                    .getObjectRequest(getRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
           
            return presignedRequest.url().toString();
            
        } catch (Exception e) {
            log.error("Error generando URL presignada: {}", e.getMessage());
            throw new RuntimeException("Error generando URL de descarga", e);
        }
    }

    public void deleteFile(String storageKey) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .build();

            s3Client.deleteObject(deleteRequest);
            log.info("Archivo eliminado de S3: {}", storageKey);
            
        } catch (Exception e) {
            log.error("Error eliminando archivo de S3: {}", e.getMessage());
            throw new RuntimeException("Error eliminando archivo de S3", e);
        }
    }

    public boolean fileExists(String storageKey) {
        try {
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(storageKey)
                    .build();

            s3Client.headObject(headRequest);
            return true;
            
        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            log.error("Error verificando existencia de archivo: {}", e.getMessage());
            return false;
        }
    }

    public String calculateSHA256(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(file.getBytes());
        StringBuilder hexString = new StringBuilder();
        
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        
        return hexString.toString();
    }

    private String generateStorageKey(Long expedienteId, UUID nodeId, String originalName, Long abogadoId) {
        String extension = "";
        int lastDot = originalName.lastIndexOf('.');
        if (lastDot > 0) {
            extension = originalName.substring(lastDot);
        }
        
        return String.format("%s/abogado-%s/expediente-%d/%s%s", 
            basePath,abogadoId, expedienteId, nodeId.toString(), extension);
    }

    private String generateStorageKeyForPlantilla(Categoria.TipoCategoria modulo, String nameCategory, String originalName) {        
        return String.format("%s/%s/%s", 
            modulo, nameCategory, originalName);
    }

    private String generateStorageKeyForSentencia(String modulo, String originalName) {        
        return String.format("%s/%s", 
            modulo, originalName);
    }
}