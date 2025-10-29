package com.bufete.backend.Dtos.folder;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

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