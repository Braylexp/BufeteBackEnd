package com.bufete.backend.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bufete.backend.Dtos.PageResponse;
import com.bufete.backend.Dtos.proceso.CreateProcesoRequest;
import com.bufete.backend.Dtos.proceso.ProcesoDTO;
import com.bufete.backend.model.Cliente;
import com.bufete.backend.model.Proceso;
import com.bufete.backend.model.Usuario;
import com.bufete.backend.repository.ClienteRepository;
import com.bufete.backend.repository.ProcesoRepository;
import com.bufete.backend.repository.UsuarioRepository;
import com.bufete.backend.utils.ProcesoMapper;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ProcesoService {
    
    private final ProcesoRepository procesoRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProcesoMapper procesoMapper;
    
    public ProcesoService(ProcesoRepository procesoRepository,
                         ClienteRepository clienteRepository,
                         UsuarioRepository usuarioRepository,
                         ProcesoMapper procesoMapper) {
        this.procesoRepository = procesoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.procesoMapper = procesoMapper;
    }
    
    @Transactional(readOnly = true)
    public PageResponse<ProcesoDTO> getAllProcesos(int page, int size, String sortBy, String sortDir,
                                                  String nombre, String numeroProceso, 
                                                  Proceso.EstadoProceso estado, Long clienteId, 
                                                  Long abogadoId, Boolean activo) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Proceso> procesosPage = procesoRepository.findWithFilters(
                nombre, numeroProceso, estado, clienteId, abogadoId, activo, pageable);
        
        List<ProcesoDTO> procesosDTOs = procesosPage.getContent().stream()
                .map(proceso -> {
                    ProcesoDTO dto = procesoMapper.toDTO(proceso);
                    dto.setTotalExpedientes(procesoRepository.countExpedientesByProcesoId(proceso.getId()));
                    dto.setTotalEventos(procesoRepository.countEventosByProcesoId(proceso.getId()));
                    return dto;
                })
                .collect(Collectors.toList());
        
        return PageResponse.<ProcesoDTO>builder()
                .content(procesosDTOs)
                .page(procesosPage.getNumber())
                .size(procesosPage.getSize())
                .totalElements(procesosPage.getTotalElements())
                .totalPages(procesosPage.getTotalPages())
                .first(procesosPage.isFirst())
                .last(procesosPage.isLast())
                .empty(procesosPage.isEmpty())
                .build();
    }
    
    @Transactional(readOnly = true)
    public ProcesoDTO getProcesoById(Long id) {
        Proceso proceso = procesoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proceso no encontrado con ID: " + id));
        
        ProcesoDTO dto = procesoMapper.toDTO(proceso);
        dto.setTotalExpedientes(procesoRepository.countExpedientesByProcesoId(id));
        dto.setTotalEventos(procesoRepository.countEventosByProcesoId(id));
        
        return dto;
    }
    
    public ProcesoDTO createProceso(CreateProcesoRequest request, Long createdById) {
        // Validaciones
        if (procesoRepository.existsByNumeroProceso(request.getNumeroProceso())) {
            throw new ValidationException("Ya existe un proceso con el número: " + request.getNumeroProceso());
        }
        
        /* Cliente cliente = clienteRepository.findByIdentificacion(request.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + request.getClienteId())); */
        
        Usuario abogado = usuarioRepository.findById(request.getAbogadoResponsableId())
                .orElseThrow(() -> new EntityNotFoundException("Abogado no encontrado con ID: " + request.getAbogadoResponsableId()));
        
        Usuario createdBy = usuarioRepository.findById(createdById)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + createdById));
        
        Cliente clienteaux = new Cliente();
        if(!clienteRepository.existsByIdentificacion(request.getClienteId())){
            System.out.println("cliente NO encontrado: ");
            
            clienteaux.setIdentificacion(request.getClienteId());
            clienteaux.setTipoCliente(Cliente.TipoCliente.NATURAL);
            clienteaux.setNombre("N/A");
            clienteaux.setTipoDocumento(request.getTipoDocumentoCliente());
            clienteaux.setCreatedBy(createdBy);
            clienteRepository.save(clienteaux);
        }
        else{
            clienteaux = clienteRepository.findByIdentificacion(request.getClienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + request.getClienteId()));
        }

        Proceso proceso = procesoMapper.toEntity(request);
        proceso.setCliente(clienteaux);
        proceso.setAbogadoResponsable(abogado);
        proceso.setCreatedBy(createdBy);
        
        Proceso savedProceso = procesoRepository.save(proceso);
        log.info("Proceso creado: {} (ID: {})", savedProceso.getNumeroProceso(), savedProceso.getId());
        
        return procesoMapper.toDTO(savedProceso);
    }
    
    public ProcesoDTO updateProceso(Long id, CreateProcesoRequest request) {
        Proceso proceso = procesoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proceso no encontrado con ID: " + id));
        
        // Validar número de proceso si cambió
        if (!request.getNumeroProceso().equals(proceso.getNumeroProceso())) {
            if (procesoRepository.existsByNumeroProceso(request.getNumeroProceso())) {
                throw new ValidationException("Ya existe un proceso con el número: " + request.getNumeroProceso());
            }
        }
        
        //TODO: arreglar cambio de getclienteID a busqueda por cliente identificacion

        // Validar referencias
        if (!request.getClienteId().equals(proceso.getCliente().getId())) {
            Cliente cliente = clienteRepository.findByIdentificacion(request.getClienteId())
                    .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con ID: " + request.getClienteId()));
            proceso.setCliente(cliente);
        }
        
        if (!request.getAbogadoResponsableId().equals(proceso.getAbogadoResponsable().getId())) {
            Usuario abogado = usuarioRepository.findById(request.getAbogadoResponsableId())
                    .orElseThrow(() -> new EntityNotFoundException("Abogado no encontrado con ID: " + request.getAbogadoResponsableId()));
            proceso.setAbogadoResponsable(abogado);
        }
        
        procesoMapper.updateEntity(proceso, request);
        Proceso updatedProceso = procesoRepository.save(proceso);
        log.info("Proceso actualizado: {} (ID: {})", updatedProceso.getNumeroProceso(), updatedProceso.getId());
        
        return procesoMapper.toDTO(updatedProceso);
    }
    
    public void deleteProceso(Long id) {
        Proceso proceso = procesoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proceso no encontrado con ID: " + id));
        
        // Soft delete
        proceso.setActivo(false);
        procesoRepository.save(proceso);
        log.info("Proceso desactivado: {} (ID: {})", proceso.getNumeroProceso(), proceso.getId());
    }
    
    public ProcesoDTO updateEstadoProceso(Long id, Proceso.EstadoProceso nuevoEstado) {
        Proceso proceso = procesoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proceso no encontrado con ID: " + id));
        
        Proceso.EstadoProceso estadoAnterior = proceso.getEstado();
        proceso.setEstado(nuevoEstado);
        
        // Si se cierra el proceso, establecer fecha de cierre
        if (nuevoEstado == Proceso.EstadoProceso.CERRADO || nuevoEstado == Proceso.EstadoProceso.ARCHIVADO) {
            proceso.setFechaCierre(LocalDate.now());
        }
        
        Proceso updatedProceso = procesoRepository.save(proceso);
        log.info("Estado del proceso {} cambiado de {} a {}", proceso.getNumeroProceso(), estadoAnterior, nuevoEstado);
        
        return procesoMapper.toDTO(updatedProceso);
    }
    
    @Transactional(readOnly = true)
    public List<ProcesoDTO> getProcesosByCliente(Long clienteId) {
        List<Proceso> procesos = procesoRepository.findByClienteIdAndActivoTrue(clienteId);
        return procesoMapper.toDTOList(procesos);
    }
    
    @Transactional(readOnly = true)
    public List<ProcesoDTO> getProcesosByAbogado(Long abogadoId) {
        List<Proceso> procesos = procesoRepository.findByAbogadoResponsableIdAndActivoTrue(abogadoId);
        return procesoMapper.toDTOList(procesos);
    }
}
