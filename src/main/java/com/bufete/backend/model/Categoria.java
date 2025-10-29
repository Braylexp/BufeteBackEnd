package com.bufete.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "categoria", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nombre", "tipo"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoCategoria tipo;
    
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;
    
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;
    
    @OneToMany(mappedBy = "categoria")
    private Set<Plantilla> plantillas;
    
    @OneToMany(mappedBy = "categoria")
    private Set<DocumentoContable> documentosContables;

    public Categoria(Boolean activo, Instant createdAt, String descripcion, Long id, String nombre, Set<Plantilla> plantillas, TipoCategoria tipo, Instant updatedAt) {
        this.activo = activo;
        this.createdAt = createdAt;
        this.descripcion = descripcion;
        this.id = id;
        this.nombre = nombre;
        this.plantillas = plantillas;
        this.tipo = tipo;
        this.updatedAt = updatedAt;
    }
    
    public enum TipoCategoria {
        PLANTILLA, CONTABLE, PROCESO
    }

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

    public TipoCategoria getTipo() {
        return tipo;
    }

    public void setTipo(TipoCategoria tipo) {
        this.tipo = tipo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    public Set<Plantilla> getPlantillas() {
        return plantillas;
    }

    public void setPlantillas(Set<Plantilla> plantillas) {
        this.plantillas = plantillas;
    }

    public Set<DocumentoContable> getDocumentosContables() {
        return documentosContables;
    }

    public void setDocumentosContables(Set<DocumentoContable> documentosContables) {
        this.documentosContables = documentosContables;
    }

    
}