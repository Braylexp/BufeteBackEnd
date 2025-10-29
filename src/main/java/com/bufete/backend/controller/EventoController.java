package com.bufete.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bufete.backend.Dtos.ApiResponse;
import com.bufete.backend.Dtos.PageResponse;
import com.bufete.backend.Dtos.evento.CreateEventoRequest;
import com.bufete.backend.Dtos.evento.EventoDTO;
import com.bufete.backend.model.Usuario;
import com.bufete.backend.repository.UsuarioRepository;
import com.bufete.backend.service.EventoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/eventos")
@Tag(name = "Eventos", description = "Gestión de eventos y calendario")
@Slf4j
public class EventoController {
    
    private final EventoService eventoService;
    private final UsuarioRepository usuarioRepository;
    
    public EventoController(EventoService eventoService, UsuarioRepository usuarioRepository) {
        this.eventoService = eventoService;
        this.usuarioRepository = usuarioRepository;
    }
    
   /*  @GetMapping
    @Operation(summary = "Listar eventos", description = "Obtiene una lista paginada de eventos")
    public ResponseEntity<ApiResponse<PageResponse<EventoDTO>>> getAllEventos(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
            @RequestParam(defaultValue = "fechaInicio") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Long tipoEventoId,
            @RequestParam(required = false) Long responsableId,
            @RequestParam(required = false) Long procesoId) {
        
        PageResponse<EventoDTO> eventos = eventoService.getAllEventos(
                page, size, sortBy, sortDir, titulo, tipoEventoId, responsableId, 
                procesoId);
        
        return ResponseEntity.ok(ApiResponse.success(eventos, 
            "Eventos obtenidos exitosamente"));
    } */

    @GetMapping("/{id}")
    @Operation(summary = "Obtener evento por ID")
    public ResponseEntity<ApiResponse<EventoDTO>> getEventoById(@PathVariable @Positive Long id) {
        EventoDTO evento = eventoService.getEventoById(id);
        return ResponseEntity.ok(ApiResponse.success(evento, "Evento obtenido exitosamente"));
    }
    
    @GetMapping("/calendar")
    @Operation(summary = "Obtener eventos para calendario", description = "Obtiene eventos en un rango de fechas")
    public ResponseEntity<ApiResponse<List<EventoDTO>>> getEventosForCalendar(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        
        List<EventoDTO> eventos = eventoService.getEventosInDateRange(fechaInicio, fechaFin);
        return ResponseEntity.ok(ApiResponse.success(eventos, "Eventos del calendario obtenidos exitosamente"));
    }
    
    @PostMapping
    @Operation(summary = "Crear evento")
    public ResponseEntity<ApiResponse<EventoDTO>> createEvento(
            @Valid @RequestBody CreateEventoRequest request,
            Authentication authentication) {

        Long createdById = getUserIdFromAuthentication(authentication);

        EventoDTO evento = eventoService.createEvento(request, createdById);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(evento, "Evento creado exitosamente"));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar evento")
    public ResponseEntity<ApiResponse<EventoDTO>> updateEvento(
            @PathVariable @Positive Long id,
            @Valid @RequestBody CreateEventoRequest request) {
        
        EventoDTO evento = eventoService.updateEvento(id, request);
        return ResponseEntity.ok(ApiResponse.success(evento, "Evento actualizado exitosamente"));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar evento")
    public ResponseEntity<ApiResponse<Void>> deleteEvento(@PathVariable @Positive Long id) {
        eventoService.deleteEvento(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Evento eliminado exitosamente"));
    }
    
    @GetMapping("/usuario")
    @Operation(summary = "Obtener eventos por usuario")
    public ResponseEntity<ApiResponse<List<EventoDTO>>> getEventosByUsuario(
            Authentication authentication) {
        Long usuarioId = getUserIdFromAuthentication(authentication);

        List<EventoDTO> eventos = eventoService.getEventosByUsuario(usuarioId);
        return ResponseEntity.ok(ApiResponse.success(eventos, "Eventos del usuario obtenidos exitosamente"));
    }
    
    @GetMapping("/proceso/{procesoId}")
    @Operation(summary = "Obtener eventos por proceso")
    public ResponseEntity<ApiResponse<List<EventoDTO>>> getEventosByProceso(
            @PathVariable @Positive Long procesoId) {
        
        List<EventoDTO> eventos = eventoService.getEventosByProceso(procesoId);
        return ResponseEntity.ok(ApiResponse.success(eventos, "Eventos del proceso obtenidos exitosamente"));
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado");
        }

        // Obtener el email del usuario autenticado
        String email = authentication.getName();

        // Buscar el usuario en la base de datos por email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + email));

        return usuario.getId();
    }
}