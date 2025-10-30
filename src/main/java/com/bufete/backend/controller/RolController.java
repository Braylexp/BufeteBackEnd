package com.bufete.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bufete.backend.Dtos.rol.InfoRolDTO;
import com.bufete.backend.Dtos.rol.RolDTO;
import com.bufete.backend.Dtos.rol.RolUpdateDTO;
import com.bufete.backend.model.Permiso;
import com.bufete.backend.model.Rol;
import com.bufete.backend.service.RolService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping ("/api/rol")
@RequiredArgsConstructor
public class RolController {
    private final RolService rolService;

    @PostMapping
    public ResponseEntity<InfoRolDTO> create(@RequestBody RolDTO request) {
        Rol rol = rolService.crearRol(request.nombre(), request.permisos());

        InfoRolDTO responseDTO = new InfoRolDTO(rol.getId(), rol.getNombre(), 
        rol.getPermisos().stream().map(Permiso::getNombre).toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/{id}")
    public ResponseEntity<InfoRolDTO> updatePermissions(@PathVariable Long id, @RequestBody RolUpdateDTO request) {
        InfoRolDTO updated = rolService.actualizarPermisosRol(id, request.getPermisos());
        return ResponseEntity.ok(updated);        
    }

    @GetMapping
    public List<RolDTO> list() {
        return rolService.listarRoles();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProceso(@PathVariable Long id){
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> obtenerProceso(@PathVariable Long id){
        Optional<Rol> proceso = rolService.obtenerRol(id);
        return proceso.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

}
