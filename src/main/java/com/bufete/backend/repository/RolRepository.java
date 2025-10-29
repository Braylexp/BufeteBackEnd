package com.bufete.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bufete.backend.Dtos.rol.RolConPermisoDTO;
import com.bufete.backend.model.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
    Optional<Rol> findById(Long rol_id);

    @Query("SELECT DISTINCT r FROM Rol r LEFT JOIN FETCH r.permisos")
    List<Rol> obtenerRolesConPermisos();
}
