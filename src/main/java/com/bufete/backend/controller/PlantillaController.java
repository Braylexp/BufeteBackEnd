package com.bufete.backend.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.bufete.backend.Dtos.plantilla.PlantillaDto;
import com.bufete.backend.model.Categoria.TipoCategoria;
import com.bufete.backend.service.PlantillaService;

@RestController
@RequestMapping ("/api/plantilla")
public class PlantillaController {
    private final PlantillaService plantillaService;

    public PlantillaController (PlantillaService plantillaService){
        this.plantillaService = plantillaService;
    }

    @GetMapping
    public List<PlantillaDto> list() {
        return plantillaService.listarArchivosIndependientes();
    }

    @GetMapping("/modulo")
    public ResponseEntity<List<UUID>> listarFileBlobsPorModulo(
            @RequestParam TipoCategoria modulo) {

        List<UUID> fileBlobIds = plantillaService.listarFileBlobsPorModulo(modulo);
        return ResponseEntity.ok(fileBlobIds);
    }
}
