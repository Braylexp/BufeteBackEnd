package com.bufete.backend.Dtos.evento;

import java.time.Instant;

import jakarta.validation.constraints.Future;
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
public class CreateEventoRequest {
    @NotBlank(message = "El título es obligatorio")
    private String titulo;
    
    private String descripcion;
    
    @NotNull(message = "El tipo de evento es obligatorio")
    private Long tipoEventoId;
    
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future(message = "La fecha de inicio debe ser futura")
    private Instant fechaInicio;
    
    private Instant fechaFin;
    
    private boolean allDay;
    private Long procesoId;
    private Long expedienteId;
}