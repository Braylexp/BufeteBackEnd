package com.bufete.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.bufete.backend.model.FileBlob;

@Repository
public interface FileBlobRepository extends JpaRepository<FileBlob, UUID> {
    
    Optional<FileBlob> findByChecksumSha256AndSizeBytes(String checksum, Long sizeBytes);
    
    @Query("SELECT fb FROM FileBlob fb WHERE fb.mimeType LIKE 'image/%'")
    List<FileBlob> findAllImages();
    
    @Query("SELECT COUNT(fv) FROM FileVersion fv WHERE fv.blob.id = :blobId")
    long countUsageByBlobId(@Param("blobId") UUID blobId);

    boolean existsByOriginalName(String originalName);
}