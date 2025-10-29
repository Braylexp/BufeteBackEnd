package com.bufete.backend.Dtos.rol;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
public class InfoRolDTO {
    private Long id;
    private String nombre;
    private List<String> permisos;
    public InfoRolDTO(Long id, String nombre, List<String> permisos) {
        this.id = id;
        this.nombre = nombre;
        this.permisos = permisos;
    }
}
