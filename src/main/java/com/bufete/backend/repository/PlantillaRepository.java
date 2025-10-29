package com.bufete.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bufete.backend.model.Categoria.TipoCategoria;
import com.bufete.backend.model.Plantilla;

@Repository
public interface PlantillaRepository extends JpaRepository<Plantilla, Long> {

    @Query("SELECT p.fileBlob.id FROM Plantilla p WHERE p.categoria.tipo = :modulo")
    List<UUID> listarFileBlobsPorModulo(@Param("modulo") TipoCategoria modulo);
}