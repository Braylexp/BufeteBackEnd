package com.bufete.backend.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bufete.backend.model.Categoria;
import com.bufete.backend.model.Categoria.TipoCategoria;


public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);

    List<Categoria> findByTipo(TipoCategoria tipo);
}



