package com.bufete.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bufete.backend.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long>, JpaSpecificationExecutor<Cliente> {
    
    Optional<Cliente> findByIdentificacion(String identificacion);
    
    boolean existsByIdentificacion(String identificacion);
    
    @Query("SELECT c FROM Cliente c WHERE c.activo = true")
    Page<Cliente> findAllActive(Pageable pageable);
    
    @Query("SELECT c FROM Cliente c WHERE " +
           "(:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:identificacion IS NULL OR c.identificacion LIKE CONCAT('%', :identificacion, '%')) AND " +
           "(:tipoCliente IS NULL OR c.tipoCliente = :tipoCliente) AND " +
           "(:activo IS NULL OR c.activo = :activo)")
    Page<Cliente> findWithFilters(@Param("nombre") String nombre,
                                 @Param("identificacion") String identificacion,
                                 @Param("tipoCliente") Cliente.TipoCliente tipoCliente,
                                 @Param("activo") Boolean activo,
                                 Pageable pageable);
}