package com.bufete.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bufete.backend.Dtos.permiso.PermisoDTO;
import com.bufete.backend.service.PermisoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping ("/api/permisos")
@RequiredArgsConstructor
public class PermisosController {
    private final PermisoService permisoService;

    @GetMapping
    public List<PermisoDTO> list() {
        return permisoService.listarPermisos();
    }
}
