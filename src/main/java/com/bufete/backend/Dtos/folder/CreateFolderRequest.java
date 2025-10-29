package com.bufete.backend.Dtos.folder;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFolderRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    
    private String description;
    
    @NotNull(message = "El nodo padre es obligatorio")
    private UUID parentId;
}
