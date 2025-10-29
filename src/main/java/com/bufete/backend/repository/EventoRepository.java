package com.bufete.backend.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bufete.backend.model.Evento;
import com.bufete.backend.model.Usuario;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>, JpaSpecificationExecutor<Evento> {

       @Query("SELECT e FROM Evento e WHERE e.isDeleted = false")
       Page<Evento> findAllActive(Pageable pageable);

       @Query("SELECT e FROM Evento e WHERE e.responsable.id = :responsableId AND e.isDeleted = false")
       List<Evento> findByResponsableIdAndIsDeletedFalse(@Param("responsableId") Long responsableId);

       @Query("SELECT e FROM Evento e WHERE e.proceso.id = :procesoId AND e.isDeleted = false")
       List<Evento> findByProcesoIdAndIsDeletedFalse(@Param("procesoId") Long procesoId);

       @Query("SELECT e FROM Evento e WHERE " +
                     "e.isDeleted = false AND " +
                     "e.fechaInicio BETWEEN :fechaInicio AND :fechaFin")
       List<Evento> findEventosInDateRange(@Param("fechaInicio") Instant fechaInicio,
                     @Param("fechaFin") Instant fechaFin);

       @Query("SELECT e FROM Evento e WHERE " +
                     "e.isDeleted = false AND " +
                     "(:titulo IS NULL OR LOWER(e.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))) AND " +
                     "(:tipoEventoId IS NULL OR e.tipoEvento.id = :tipoEventoId) AND " +
                     "(:responsableId IS NULL OR e.responsable.id = :responsableId) AND " +
                     "(:procesoId IS NULL OR e.proceso.id = :procesoId)")
       Page<Evento> findWithFilters(@Param("titulo") String titulo,
                     @Param("tipoEventoId") Long tipoEventoId,
                     @Param("responsableId") Long responsableId,
                     @Param("procesoId") Long procesoId,
                     Pageable pageable);
       
       List<Evento> findByResponsable(Usuario responsable);

}
