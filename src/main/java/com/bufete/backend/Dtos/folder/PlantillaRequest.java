package com.bufete.backend.Dtos.folder;

import org.springframework.web.multipart.MultipartFile;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlantillaRequest {
    private MultipartFile file;
    private Long idCategory;
    private String description;
    private String nombre;
}