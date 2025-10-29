package com.bufete.backend.Dtos;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class FileUploadResponse {
    private UUID nodeId;
    private String name;
    private Long sizeBytes;
    private String mimeType;
    private Integer versionNumber;
    private String message;
    
}