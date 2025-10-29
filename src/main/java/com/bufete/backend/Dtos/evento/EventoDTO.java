package com.bufete.backend.Dtos.evento;

import java.time.Instant;

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
public class EventoDTO {
    private Long id;
    
    @NotBlank(message = "El título es obligatorio")
    private String titulo;
    
    private String descripcion;
    
    @NotNull(message = "El tipo de evento es obligatorio")
    private Long tipoEventoId;
    private String tipoEventoNombre;
    private String tipoEventoColor;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    private Instant fechaInicio;
    
    private Instant fechaFin;
    private Boolean allDay;
    
    // Referencias opcionales
    private Long procesoId;
    private String procesoNombre;
    private Long expedienteId;
    private String expedienteNombre;
    
    private Long responsableId;
    private String responsableNombre;
    
    private Instant createdAt;
    private Instant updatedAt;
}