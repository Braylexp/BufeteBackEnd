package com.bufete.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bufete.backend.Dtos.categoria.CategoriaDTO;
import com.bufete.backend.model.Categoria.TipoCategoria;
import com.bufete.backend.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping ("/api/categoria")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoryService categoryService;

    @PostMapping
    public List<CategoriaDTO> listCatModulo(@RequestBody String  modulo) {
        TipoCategoria tipo = TipoCategoria.valueOf(modulo);
        return categoryService.listarCategoriasModulo(tipo);
    }
}
