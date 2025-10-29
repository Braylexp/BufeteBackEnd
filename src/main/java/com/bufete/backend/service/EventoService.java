package com.bufete.backend.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bufete.backend.Dtos.ApiResponse;
import com.bufete.backend.Dtos.PageResponse;
import com.bufete.backend.Dtos.evento.CreateEventoRequest;
import com.bufete.backend.Dtos.evento.EventoDTO;
import com.bufete.backend.model.Evento;
import com.bufete.backend.model.Expediente;
import com.bufete.backend.model.Proceso;
import com.bufete.backend.model.TipoEvento;
import com.bufete.backend.model.Usuario;
import com.bufete.backend.repository.EventoRepository;
import com.bufete.backend.repository.ExpedienteRepository;
import com.bufete.backend.repository.ProcesoRepository;
import com.bufete.backend.repository.TipoEventoRepository;
import com.bufete.backend.repository.UsuarioRepository;
import com.bufete.backend.utils.EventoMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class EventoService {
    
    private final EventoRepository eventoRepository;
    private final TipoEventoRepository tipoEventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProcesoRepository procesoRepository;
    private final ExpedienteRepository expedienteRepository;
    private final EventoMapper eventoMapper;
    
    public EventoService(EventoRepository eventoRepository,
                        TipoEventoRepository tipoEventoRepository,
                        UsuarioRepository usuarioRepository,
                        ProcesoRepository procesoRepository,
                        ExpedienteRepository expedienteRepository,
                        EventoMapper eventoMapper) {
        this.eventoRepository = eventoRepository;
        this.tipoEventoRepository = tipoEventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.procesoRepository = procesoRepository;
        this.expedienteRepository = expedienteRepository;
        this.eventoMapper = eventoMapper;
    }
    
    @Transactional(readOnly = true)
    public List<Evento> getEventsByResponsable(Long userId){
        return eventoRepository.findByResponsableIdAndIsDeletedFalse(userId);
    }

    @Transactional(readOnly = true)
    public PageResponse<EventoDTO> getAllEventos(int page, int size, String sortBy, String sortDir,
                                               String titulo, Long tipoEventoId, Long responsableId, Long procesoId) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Evento> eventosPage = eventoRepository.findWithFilters(titulo, tipoEventoId, responsableId, procesoId, pageable);
        List<EventoDTO> eventosDTOs = eventoMapper.toDTOList(eventosPage.getContent());
        
        return PageResponse.of(eventosPage.map(eventoMapper::toDTO));
    }
    
    @Transactional(readOnly = true)
    public EventoDTO getEventoById(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + id));
        
        if (evento.getIsDeleted()) {
            throw new EntityNotFoundException("El evento ha sido eliminado");
        }
        
        return eventoMapper.toDTO(evento);
    }
    
    @Transactional(readOnly = true)
    public List<EventoDTO> getEventosInDateRange(LocalDate fechaInicio, LocalDate fechaFin) {
        Instant inicio = fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant fin = fechaFin.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        
        List<Evento> eventos = eventoRepository.findEventosInDateRange(inicio, fin);
        return eventoMapper.toDTOList(eventos);
    }
    
    public EventoDTO createEvento(CreateEventoRequest request, Long userId) {
        // Validaciones
        TipoEvento tipoEvento = tipoEventoRepository.findById(request.getTipoEventoId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de evento no encontrado"));
        
        Usuario responsable = usuarioRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario responsable no encontrado"));
        
        // Validar fechas
        if (request.getFechaFin() != null && request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new ValidationException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        
        Evento evento = eventoMapper.toEntity(request);
        evento.setTipoEvento(tipoEvento);
        evento.setResponsable(responsable);
        
        // Referencias opcionales
        if (request.getProcesoId() != null) {
            Proceso proceso = procesoRepository.findById(request.getProcesoId())
                    .orElseThrow(() -> new EntityNotFoundException("Proceso no encontrado"));
            evento.setProceso(proceso);
        }
        
        if (request.getExpedienteId() != null) {
            Expediente expediente = expedienteRepository.findById(request.getExpedienteId())
                    .orElseThrow(() -> new EntityNotFoundException("Expediente no encontrado"));
            evento.setExpediente(expediente);
        }
        
        Evento savedEvento = eventoRepository.save(evento);
        log.info("Evento creado: {} (ID: {})", savedEvento.getTitulo(), savedEvento.getId());
        
        return eventoMapper.toDTO(savedEvento);
    }
    
    public EventoDTO updateEvento(Long id, CreateEventoRequest request) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + id));
        
        if (evento.getIsDeleted()) {
            throw new EntityNotFoundException("El evento ha sido eliminado");
        }
        
        // Validar fechas
        if (request.getFechaFin() != null && request.getFechaFin().isBefore(request.getFechaInicio())) {
            throw new ValidationException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        
        // Actualizar referencias si cambiaron
        if (!request.getTipoEventoId().equals(evento.getTipoEvento().getId())) {
            TipoEvento tipoEvento = tipoEventoRepository.findById(request.getTipoEventoId())
                    .orElseThrow(() -> new EntityNotFoundException("Tipo de evento no encontrado"));
            evento.setTipoEvento(tipoEvento);
        }
        eventoMapper.updateEntity(evento, request);
        Evento updatedEvento = eventoRepository.save(evento);
        log.info("Evento actualizado: {} (ID: {})", updatedEvento.getTitulo(), updatedEvento.getId());
        
        return eventoMapper.toDTO(updatedEvento);
    }
    
    public void deleteEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado con ID: " + id));
        
        evento.setIsDeleted(true);
        eventoRepository.save(evento);
        log.info("Evento eliminado: {} (ID: {})", evento.getTitulo(), evento.getId());
    }
    
    @Transactional(readOnly = true)
    public List<EventoDTO> getEventosByUsuario(Long usuarioId) {
        List<Evento> eventos = eventoRepository.findByResponsableIdAndIsDeletedFalse(usuarioId);
        return eventoMapper.toDTOList(eventos);
    }
    
    @Transactional(readOnly = true)
    public List<EventoDTO> getEventosByProceso(Long procesoId) {
        List<Evento> eventos = eventoRepository.findByProcesoIdAndIsDeletedFalse(procesoId);
        return eventoMapper.toDTOList(eventos);
    }
}
