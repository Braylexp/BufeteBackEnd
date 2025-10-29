package com.bufete.backend.Dtos.sentencia;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SentenciaRequest {
    private String tipoDoc;
    private MultipartFile file;
    private Long procesosId;
    private Long expedId;
    private String clienteId;
    private String description;
    private String nombre;
}