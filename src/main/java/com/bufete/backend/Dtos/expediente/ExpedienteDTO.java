package com.bufete.backend.Dtos.expediente;

import java.time.Instant;
import java.util.UUID;

import com.bufete.backend.model.Expediente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpedienteDTO {
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    private String descripcion;
    private Expediente.EstadoExpediente estado;
    private Instant fechaCreacion;
    private Instant fechaCierre;
    
    @NotNull(message = "El proceso es obligatorio")
    private Long procesoId;
    private String procesoNombre;
    private String procesoNumero;
    
    private UUID rootNodeId;
    private Integer orden;
    private String createdByNombre;
    private Instant updatedAt;
    
    // Campos adicionaless
    private int totalDocumentos;
    private long totalSize;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public Expediente.EstadoExpediente getEstado() {
        return estado;
    }
    public void setEstado(Expediente.EstadoExpediente estado) {
        this.estado = estado;
    }
    public Instant getFechaCreacion() {
        return fechaCreacion;
    }
    public void setFechaCreacion(Instant fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public Instant getFechaCierre() {
        return fechaCierre;
    }
    public void setFechaCierre(Instant fechaCierre) {
        this.fechaCierre = fechaCierre;
    }
    public Long getProcesoId() {
        return procesoId;
    }
    public void setProcesoId(Long procesoId) {
        this.procesoId = procesoId;
    }
    public String getProcesoNombre() {
        return procesoNombre;
    }
    public void setProcesoNombre(String procesoNombre) {
        this.procesoNombre = procesoNombre;
    }
    public String getProcesoNumero() {
        return procesoNumero;
    }
    public void setProcesoNumero(String procesoNumero) {
        this.procesoNumero = procesoNumero;
    }
    public UUID getRootNodeId() {
        return rootNodeId;
    }
    public void setRootNodeId(UUID rootNodeId) {
        this.rootNodeId = rootNodeId;
    }
    public Integer getOrden() {
        return orden;
    }
    public void setOrden(Integer orden) {
        this.orden = orden;
    }
    public String getCreatedByNombre() {
        return createdByNombre;
    }
    public void setCreatedByNombre(String createdByNombre) {
        this.createdByNombre = createdByNombre;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
    public int getTotalDocumentos() {
        return totalDocumentos;
    }
    public void setTotalDocumentos(int totalDocumentos) {
        this.totalDocumentos = totalDocumentos;
    }
    public long getTotalSize() {
        return totalSize;
    }
    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    
}