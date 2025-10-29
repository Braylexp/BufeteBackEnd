package com.bufete.backend.utils;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.bufete.backend.Dtos.usuario.CreateUsuarioRequest;
import com.bufete.backend.Dtos.usuario.UpdateUsuarioRequest;
import com.bufete.backend.Dtos.usuario.UsuarioDTO;
import com.bufete.backend.model.Usuario;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    
    @Mapping(target = "rolNombre", source = "rol.nombre")
    @Mapping(target = "rolId", source = "rol.id")
    UsuarioDTO toDTO(Usuario usuario);
    
    List<UsuarioDTO> toDTOList(List<Usuario> usuarios);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "nuevoUsuario", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastLogin", ignore = true)
    @Mapping(target = "clientesCreados", ignore = true)
    @Mapping(target = "procesosComoAbogado", ignore = true)
    Usuario toEntity(CreateUsuarioRequest request);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Usuario usuario, UpdateUsuarioRequest request);
}