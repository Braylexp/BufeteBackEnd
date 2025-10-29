package com.bufete.backend.Dtos.folder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadUrlDTO {
    private String url;
    private String fileName;
    private String mimeType;
    private Long sizeBytes;
    private Integer expiresInSeconds;
}
