package com.bufete.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bufete.backend.Dtos.categoria.CategoriaDTO;
import com.bufete.backend.model.Categoria;
import com.bufete.backend.model.Categoria.TipoCategoria;
import com.bufete.backend.repository.CategoriaRepository;

@Service
public class CategoryService {

    private final CategoriaRepository categoriaRepository;

    public CategoryService(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
        
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarCategorias(){

        List<Categoria> category = categoriaRepository.findAll();
        List<CategoriaDTO> listCats = new ArrayList<>();

        for (Categoria categoria : category) {
            CategoriaDTO catAux = new CategoriaDTO();
            catAux.setId(categoria.getId());
            catAux.setNombre(categoria.getNombre());
            catAux.setTipo(categoria.getTipo().toString());

            listCats.add(catAux);
        }
        return listCats;
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarCategoriasModulo(TipoCategoria modulo){

        List<Categoria> category = categoriaRepository.findByTipo(modulo);
        List<CategoriaDTO> listCats = new ArrayList<>();

        for (Categoria categoria : category) {
            CategoriaDTO catAux = new CategoriaDTO();
            catAux.setId(categoria.getId());
            catAux.setNombre(categoria.getNombre());
            catAux.setTipo(categoria.getTipo().toString());

            listCats.add(catAux);
        }
        return listCats;
    }
}
