package com.bufete.backend.Dtos;

import lombok.Data;
import java.util.UUID;

@Data
public class UploadFileRequest {
    private String namesda;
    private String description;
    private UUID parentId;
    private String note;
}