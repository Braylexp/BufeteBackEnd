package com.bufete.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "sentencia", indexes = {
    @Index(name = "idx_sentencia_proceso", columnList = "proceso_id"),
    @Index(name = "idx_sentencia_cliente", columnList = "cliente_id"),
    @Index(name = "idx_sentencia_abogado", columnList = "abogado_id"),
    @Index(name = "idx_sentencia_fecha", columnList = "fecha_sentencia")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sentencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_sentencia", length = 100)
    private String numeroSentencia;
    
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "tipo_sentencia", nullable = false, length = 50)
    private String tipoSentencia;
    
    @Column(name = "fecha_sentencia", nullable = false)
    private LocalDate fechaSentencia;
    
    @Column(name = "fecha_notificacion")
    private LocalDate fechaNotificacion;
    
    @Column(name = "juzgado", length = 200)
    private String juzgado;
    
    @Column(name = "magistrado", length = 200)
    private String magistrado;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proceso_id", nullable = false)
    private Proceso proceso;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "expediente_id")
    private Expediente expediente;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "abogado_id", nullable = false)
    private Usuario abogado;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    @Builder.Default
    private EstadoSentencia estado = EstadoSentencia.PRIMERA_INSTANCIA;
    
    @Column(name = "es_favorable")
    private Boolean esFavorable;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_blob_id", nullable = false)
    private FileBlob fileBlob;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by", nullable = false)
    private Usuario createdBy;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;
    
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;
    
    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;
    
    public enum EstadoSentencia {
        PRIMERA_INSTANCIA, SEGUNDA_INSTANCIA, CASACION,
        EJECUTORIADA, APELADA, CUMPLIDA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public Expediente getExpediente() {
        return expediente;
    }

    public void setExpediente(Expediente expediente) {
        this.expediente = expediente;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getAbogado() {
        return abogado;
    }

    public void setAbogado(Usuario abogado) {
        this.abogado = abogado;
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

    public FileBlob getFileBlob() {
        return fileBlob;
    }

    public void setFileBlob(FileBlob fileBlob) {
        this.fileBlob = fileBlob;
    }

    public Usuario getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Usuario createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    
}