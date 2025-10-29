package com.bufete.backend.utils;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.bufete.backend.Dtos.evento.CreateEventoRequest;
import com.bufete.backend.Dtos.evento.EventoDTO;
import com.bufete.backend.model.Evento;

/**
 * Mapper para convertir entre entidades Evento y sus DTOs correspondientes
 * Utiliza MapStruct para generar automáticamente las implementaciones
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EventoMapper {
    
    /**
     * Convierte una entidad Evento a EventoDTO
     * Mapea las relaciones a sus IDs y nombres correspondientes
     */
    @Mapping(target = "tipoEventoId", source = "tipoEvento.id")
    @Mapping(target = "tipoEventoNombre", source = "tipoEvento.nombre")
    @Mapping(target = "tipoEventoColor", source = "tipoEvento.color")
    @Mapping(target = "procesoId", source = "proceso.id")
    @Mapping(target = "procesoNombre", source = "proceso.nombre")
    @Mapping(target = "expedienteId", source = "expediente.id")
    @Mapping(target = "expedienteNombre", source = "expediente.nombre")
    @Mapping(target = "responsableId", source = "responsable.id")
    @Mapping(target = "responsableNombre", 
             expression = "java(getFullName(evento.getResponsable().getNombre(), evento.getResponsable().getApellido()))")
    EventoDTO toDTO(Evento evento);
    
    /**
     * Convierte una lista de entidades Evento a una lista de EventoDTO
     */
    List<EventoDTO> toDTOList(List<Evento> eventos);
    
    /**
     * Convierte un CreateEventoRequest a una entidad Evento
     * Ignora campos que se setean automáticamente o por separado
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipoEvento", ignore = true)
    @Mapping(target = "proceso", ignore = true)
    @Mapping(target = "expediente", ignore = true)
    @Mapping(target = "responsable", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    Evento toEntity(CreateEventoRequest request);
    
    /**
     * Actualiza una entidad Evento existente con los datos de CreateEventoRequest
     * Utiliza IGNORE para no sobrescribir campos null
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipoEvento", ignore = true)
    @Mapping(target = "proceso", ignore = true)
    @Mapping(target = "expediente", ignore = true)
    @Mapping(target = "responsable", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    void updateEntity(@MappingTarget Evento evento, CreateEventoRequest request);
    
    /**
     * Método helper para concatenar nombre y apellido
     * Se usa en expressions de MapStruct
     */
    default String getFullName(String nombre, String apellido) {
        if (nombre == null && apellido == null) {
            return null;
        }
        if (nombre == null) {
            return apellido.trim();
        }
        if (apellido == null) {
            return nombre.trim();
        }
        return (nombre + " " + apellido).trim();
    }
    
    /**
     * Mapeo personalizado para manejar el campo allDay
     * Si no se especifica, por defecto es false
     */
    @Named("mapAllDay")
    default Boolean mapAllDay(Boolean allDay) {
        return allDay != null ? allDay : false;
    }
    
    /**
     * Convierte EventoDTO de vuelta a entidad (útil para updates parciales)
     * Solo mapea campos básicos, las relaciones se manejan por separado
     */
    @Mapping(target = "tipoEvento", ignore = true)
    @Mapping(target = "proceso", ignore = true)
    @Mapping(target = "expediente", ignore = true)
    @Mapping(target = "responsable", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    Evento toEntity(EventoDTO eventoDTO);
}