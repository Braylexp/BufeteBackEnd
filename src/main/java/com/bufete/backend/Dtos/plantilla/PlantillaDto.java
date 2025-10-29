package com.bufete.backend.Dtos.plantilla;

import java.time.Instant;
import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PlantillaDto {
    private UUID id;
    private String nombre;
    private String categoria;
    private Instant fecha;
}
