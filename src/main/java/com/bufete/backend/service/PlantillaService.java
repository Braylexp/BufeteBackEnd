package com.bufete.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bufete.backend.Dtos.plantilla.PlantillaDto;
import com.bufete.backend.model.Categoria.TipoCategoria;
import com.bufete.backend.model.Plantilla;
import com.bufete.backend.repository.PlantillaRepository;

@Service
public class PlantillaService {
    private final PlantillaRepository plantillaRepository;

    public PlantillaService (PlantillaRepository plantillaRepository){
        this.plantillaRepository = plantillaRepository;
    }

    @Transactional(readOnly = true)
    public List<PlantillaDto> listarArchivosIndependientes(){
        List<Plantilla> listaaux = plantillaRepository.findAll();
        List<PlantillaDto> lista = new ArrayList<>();

        for (Plantilla plantilla : listaaux) {   
            PlantillaDto dto= new PlantillaDto();
            dto.setId(plantilla.getFileBlob().getId());
            dto.setNombre(plantilla.getNombre());
            dto.setFecha(plantilla.getFechaCreacion());
            dto.setCategoria(plantilla.getCategoria().getNombre());

            lista.add(dto);
        }

        return lista;
    }

    @Transactional(readOnly = true)
    public List<PlantillaDto> listarArchivosPorModulo(){
        List<Plantilla> listaaux = plantillaRepository.findAll();
        List<PlantillaDto> lista = new ArrayList<>();

        for (Plantilla plantilla : listaaux) {   
            PlantillaDto dto= new PlantillaDto();
            dto.setId(plantilla.getFileBlob().getId());
            dto.setNombre(plantilla.getNombre());
            dto.setFecha(plantilla.getFechaCreacion());
            dto.setCategoria(plantilla.getCategoria().getNombre());

            lista.add(dto);
        }

        return lista;
    }

    @Transactional(readOnly = true)
    public List<UUID> listarFileBlobsPorModulo(TipoCategoria modulo) {
        return plantillaRepository.listarFileBlobsPorModulo(modulo);
    }

}
