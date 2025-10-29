package com.bufete.backend.Dtos.expediente;

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
public class CreateExpedienteRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    private String descripcion;
    
    @NotNull(message = "El proceso es obligatorio")
    private Long procesoId;
    
    private Integer orden;
}