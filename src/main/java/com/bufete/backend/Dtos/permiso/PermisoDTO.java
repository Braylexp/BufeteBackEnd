package com.bufete.backend.Dtos.permiso;

import lombok.Setter;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter 
@Setter
@NoArgsConstructor
public class PermisoDTO {
    private Long id;
    private String nombre;
    private String descripcion;

    public PermisoDTO(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
}
