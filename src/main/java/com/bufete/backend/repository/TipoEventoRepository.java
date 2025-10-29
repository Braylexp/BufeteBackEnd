package com.bufete.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bufete.backend.model.TipoEvento;

@Repository
public interface TipoEventoRepository extends JpaRepository<TipoEvento, Long> {
    
    boolean existsByNombre(String nombre);
    
    @Query("SELECT te FROM TipoEvento te WHERE te.activo = true")
    List<TipoEvento> findAllActive();
    
    @Query("SELECT te FROM TipoEvento te WHERE " +
           "(:nombre IS NULL OR LOWER(te.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:activo IS NULL OR te.activo = :activo)")
    Page<TipoEvento> findWithFilters(@Param("nombre") String nombre,
                                   @Param("activo") Boolean activo,
                                   Pageable pageable);
}
