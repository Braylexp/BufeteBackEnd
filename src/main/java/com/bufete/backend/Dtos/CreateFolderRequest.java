package com.bufete.backend.Dtos;

import lombok.Data;
import java.util.UUID;

@Data
public class CreateFolderRequest {
    private String name;
    private String description;
    private UUID parentId;
}