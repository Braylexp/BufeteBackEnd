package com.bufete.backend.Dtos.rol;

import java.util.Set;
import java.util.stream.Collectors;

import com.bufete.backend.model.Permiso;
import com.bufete.backend.model.Rol;

public record RolDTO(Long id, String nombre, Set<String> permisos) {

    public static record PermSimple(Long id, String nombre, String descripcion) {}

    public static RolDTO fromEntity(Rol rol) {
        Set<String> perms = rol.getPermisos().stream()
            .map(Permiso::getNombre)
            .collect(Collectors.toSet());

        return new RolDTO(rol.getId(), rol.getNombre(), perms);
    }
}
