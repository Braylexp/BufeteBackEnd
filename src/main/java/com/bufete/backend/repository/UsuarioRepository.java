package com.bufete.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bufete.backend.Dtos.usuario.UsuarioDTO;
import com.bufete.backend.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    
       Optional<Usuario> findByEmail(String email);
       
       Optional<Usuario> findByIdentificacion(String identificacion);
       
       boolean existsByEmail(String email);
       
       boolean existsByIdentificacion(String identificacion);
       
       @Query("SELECT new com.bufete.backend.Dtos.usuario.UsuarioDTO(u.id, u.nombre, u.apellido, u.email, u.rol.id) FROM Usuario u WHERE u.activo = true")
       List<UsuarioDTO> findAllActive();
       
       @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = :rolNombre AND u.activo = true")
       List<Usuario> findByRolNombreAndActivoTrue(@Param("rolNombre") String rolNombre);
       
       @Query("SELECT u FROM Usuario u WHERE " +
              "(:nombre IS NULL OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
              "(:apellido IS NULL OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))) AND " +
              "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
              "(:activo IS NULL OR u.activo = :activo)")
       Page<Usuario> findWithFilters(@Param("nombre") String nombre,
                                   @Param("apellido") String apellido,
                                   @Param("email") String email,
                                   @Param("activo") Boolean activo,
                     Pageable pageable);

       
}