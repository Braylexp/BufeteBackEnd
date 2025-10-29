package com.bufete.backend.Dtos;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileVersionDto {
    private UUID id;
    private UUID nodeId;
    private Integer versionNum;
    private Integer uploadedBy;
    private String uploaderName;
    private LocalDateTime uploadedAt;
    private String note;
    private Boolean isCurrent;
    private Long sizeBytes;
    private String mimeType;
    private String downloaUrl;
}