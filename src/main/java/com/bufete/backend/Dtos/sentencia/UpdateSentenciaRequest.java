package com.bufete.backend.Dtos.sentencia;

import com.bufete.backend.model.Sentencia.EstadoSentencia;
import java.time.LocalDate;
import java.util.UUID;

public class UpdateSentenciaRequest {
    
    private String numeroSentencia;
    private String nombre;
    private String descripcion;
    private String tipoSentencia;
    private LocalDate fechaSentencia;
    private LocalDate fechaNotificacion;
    private String juzgado;
    private String magistrado;
    private Long expedienteId;
    private EstadoSentencia estado;
    private Boolean esFavorable;
    private String observaciones;
    private UUID fileBlobId;

    public UpdateSentenciaRequest() {
    }

    public String getNumeroSentencia() {
        return numeroSentencia;
    }

    public void setNumeroSentencia(String numeroSentencia) {
        this.numeroSentencia = numeroSentencia;
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

    public String getTipoSentencia() {
        return tipoSentencia;
    }

    public void setTipoSentencia(String tipoSentencia) {
        this.tipoSentencia = tipoSentencia;
    }

    public LocalDate getFechaSentencia() {
        return fechaSentencia;
    }

    public void setFechaSentencia(LocalDate fechaSentencia) {
        this.fechaSentencia = fechaSentencia;
    }

    public LocalDate getFechaNotificacion() {
        return fechaNotificacion;
    }

    public void setFechaNotificacion(LocalDate fechaNotificacion) {
        this.fechaNotificacion = fechaNotificacion;
    }

    public String getJuzgado() {
        return juzgado;
    }

    public void setJuzgado(String juzgado) {
        this.juzgado = juzgado;
    }

    public String getMagistrado() {
        return magistrado;
    }

    public void setMagistrado(String magistrado) {
        this.magistrado = magistrado;
    }

    public Long getExpedienteId() {
        return expedienteId;
    }

    public void setExpedienteId(Long expedienteId) {
        this.expedienteId = expedienteId;
    }

    public EstadoSentencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoSentencia estado) {
        this.estado = estado;
    }

    public Boolean getEsFavorable() {
        return esFavorable;
    }

    public void setEsFavorable(Boolean esFavorable) {
        this.esFavorable = esFavorable;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public UUID getFileBlobId() {
        return fileBlobId;
    }

    public void setFileBlobId(UUID fileBlobId) {
        this.fileBlobId = fileBlobId;
    }
}