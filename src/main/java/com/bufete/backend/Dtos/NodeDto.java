package com.bufete.backend.Dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class NodeDto {
    private UUID id;
    private Integer expedienteIdas;
    private UUID parentId;
    private String type;
    private String name;
    private String description;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long sizeBytes;
    private Integer itemCount;
    private LocalDateTime lastAccessed;
    
    // Para archivos
    private String mimeType;
    private String originalName;
    private Integer currentVersion;
    private Boolean isImage;
    private String downloadUrl;
    private String thumbnailUrl;
    
    // Para navegación
    private List<NodeDto> children;
    private List<String> breadcrumb;
}