package com.bufete.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bufete.backend.Dtos.rol.InfoRolDTO;
import com.bufete.backend.Dtos.rol.RolConPermisoDTO;
import com.bufete.backend.Dtos.rol.RolDTO;
import com.bufete.backend.Dtos.rol.RolUpdateDTO;
import com.bufete.backend.model.Permiso;
import com.bufete.backend.model.Rol;
import com.bufete.backend.repository.PermisoRepository;
import com.bufete.backend.repository.RolRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RolService {
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;

    public RolService(RolRepository rolRepository, PermisoRepository permisoRepository) {
        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
    }

    @Transactional
    public Rol crearRol(String nombre, Set<String> permisos) {
        return rolRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    Set<Permiso> perms = permisos.stream()
                            .map(this::buscarOCrearPermisos)
                            .collect(Collectors.toSet());
                    Rol r = new Rol();
                    r.setNombre(nombre);
                    r.setPermisos(perms);
                    return rolRepository.save(r);
                });
    }

    @Transactional
    public InfoRolDTO actualizarPermisosRol(Long rol_Id, Set<String> permisos) {
        Rol role = rolRepository.findById(rol_Id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado: "+rol_Id));
        Set<Permiso> perms = new HashSet<>();
        
        for (String permiso : permisos) {
            Permiso perm = permisoRepository.findByNombre(permiso)
                .orElseThrow(() -> new EntityNotFoundException("Permiso no encontrado : "+permiso));
            perms.add(perm);
        }
        role.setPermisos(perms);
        Rol updated = rolRepository.save(role);
        InfoRolDTO rolAux = new InfoRolDTO();
        
        rolAux.setId(updated.getId());
        rolAux.setNombre(updated.getNombre());

        rolAux.setPermisos(null);
        return rolAux;
    }

    @Transactional(readOnly = true)
    public List<RolDTO> listarRoles() {

        List<Rol> resultados = rolRepository.obtenerRolesConPermisos();

        // Mapa temporal para agrupar permisos por rol
        Map<Long, Set<String>> permisosMap = new HashMap<>();
        Map<Long, String> nombresMap = new HashMap<>();

        for (Rol r : resultados) {
            Long id = r.getId();
            String nombreRol = r.getNombre();

            nombresMap.putIfAbsent(id, nombreRol);

            Set<String> permisos = permisosMap.computeIfAbsent(id, key -> new HashSet<>());

            for (Permiso permiso : r.getPermisos()) {
                permisos.add(permiso.getNombre());
            }
        }

        // Crear los records con los datos completos
        return permisosMap.entrySet().stream()
                .map(entry -> new RolDTO(
                        entry.getKey(),
                        nombresMap.get(entry.getKey()),
                        entry.getValue()))
                .collect(Collectors.toList());
    }

    private Permiso buscarOCrearPermisos(String nombre) {
        return permisoRepository.findByNombre(nombre)
                .orElseGet(() -> {
                    Permiso p = new Permiso();
                    p.setNombre(nombre);
                    return permisoRepository.save(p);
                });
    }

    @Transactional
    public void eliminarPermiso(Long id) {
        permisoRepository.deleteById(id);
    }

    @Transactional
    public void eliminarRol(Long id) {
        rolRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Rol> obtenerRol(Long id) {
        return rolRepository.findById(id);
    }
}
