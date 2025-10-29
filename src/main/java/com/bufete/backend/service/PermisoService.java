package com.bufete.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bufete.backend.Dtos.permiso.PermisoDTO;
import com.bufete.backend.repository.PermisoRepository;

@Service
public class PermisoService {
    private final PermisoRepository permisoRepository;

    public PermisoService(PermisoRepository permisoRepository){
        this.permisoRepository = permisoRepository;
    }

    @Transactional(readOnly = true)
    public List<PermisoDTO> listarPermisos(){
        return permisoRepository.listarTodosPermisos();
    }
}
