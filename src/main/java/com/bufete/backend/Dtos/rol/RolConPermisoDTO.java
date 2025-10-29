package com.bufete.backend.Dtos.rol;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
public class RolConPermisoDTO{

    private Long id;
    private String nombre;
    private String permiso;
    public RolConPermisoDTO(Long id, String nombre, String permisos) {
        this.id = id;
        this.nombre = nombre;
        this.permiso = permisos;
    }

    
}
