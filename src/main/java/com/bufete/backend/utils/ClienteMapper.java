package com.bufete.backend.utils;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.bufete.backend.Dtos.cliente.ClienteDTO;
import com.bufete.backend.Dtos.cliente.CreateClienteRequest;
import com.bufete.backend.model.Cliente;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    
    @Mapping(target = "createdByNombre", source = "createdBy.nombre")
    ClienteDTO toDTO(Cliente cliente);
    
    List<ClienteDTO> toDTOList(List<Cliente> clientes);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "procesos", ignore = true)
    @Mapping(target = "documentosContables", ignore = true)
    @Mapping(target = "sentencias", ignore = true)
    Cliente toEntity(CreateClienteRequest request);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Cliente cliente, CreateClienteRequest request);
}