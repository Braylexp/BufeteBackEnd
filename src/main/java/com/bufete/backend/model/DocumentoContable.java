package com.bufete.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "documento_contable", indexes = {
    @Index(name = "idx_doc_contable_categoria", columnList = "categoria_id"),
    @Index(name = "idx_doc_contable_cliente", columnList = "cliente_id"),
    @Index(name = "idx_doc_contable_fecha", columnList = "fecha_documento")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoContable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_documento", length = 100)
    private String numeroDocumento;
    
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proceso_id")
    private Proceso proceso;
    
    @Column(name = "fecha_documento", nullable = false)
    private LocalDate fechaDocumento;
    
    @Column(name = "root_node_id", unique = true)
    private UUID rootNodeId;
    
    @ManyToOne(fetch = FetchType.LAZY)
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
    
    @OneToMany(mappedBy = "contable", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Node> nodes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Proceso getProceso() {
        return proceso;
    }

    public void setProceso(Proceso proceso) {
        this.proceso = proceso;
    }

    public LocalDate getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(LocalDate fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public UUID getRootNodeId() {
        return rootNodeId;
    }

    public void setRootNodeId(UUID rootNodeId) {
        this.rootNodeId = rootNodeId;
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

    public Set<Node> getNodes() {
        return nodes;
    }

    public void setNodes(Set<Node> nodes) {
        this.nodes = nodes;
    }

    
}
