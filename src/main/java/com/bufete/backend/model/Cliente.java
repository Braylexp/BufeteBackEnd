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

@Entity
@Table(name = "cliente", indexes = {
    @Index(name = "idx_cliente_identificacion", columnList = "identificacion"),
    @Index(name = "idx_cliente_nombre", columnList = "nombre"),
    @Index(name = "idx_cliente_activo", columnList = "activo")
})

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false, length = 20)
    private TipoCliente tipoCliente;
    
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;
    
    @Column(name = "apellido", length = 200)
    private String apellido;
    
    @Column(name = "razon_social", length = 300)
    private String razonSocial;
    
    @Column(name = "identificacion", unique = true, nullable = false, length = 100)
    private String identificacion;
    
    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento;
    
    @Column(name = "email", length = 200)
    private String email;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "direccion")
    private String direccion;
    
    @Column(name = "ciudad", length = 100)
    private String ciudad;
    
    @Column(name = "departamento", length = 100)
    private String departamento;
    
    @Column(name = "pais", length = 100)
    @Builder.Default
    private String pais = "Colombia";
    
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column(name = "representante_legal", length = 200)
    private String representanteLegal;
    
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Usuario createdBy;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;
    
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private Instant updatedAt;
    
    @OneToMany(mappedBy = "cliente")
    @Builder.Default
    private Set<Proceso> procesos = new HashSet<>();
    
    @OneToMany(mappedBy = "cliente")
    @Builder.Default
    private Set<DocumentoContable> documentosContables = new HashSet<>();
    
    @OneToMany(mappedBy = "cliente")
    @Builder.Default
    private Set<Sentencia> sentencias = new HashSet<>();
    
    public enum TipoCliente {
        NATURAL, JURIDICA
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public Set<Proceso> getProcesos() {
        return procesos;
    }

    public void setProcesos(Set<Proceso> procesos) {
        this.procesos = procesos;
    }

    public Set<DocumentoContable> getDocumentosContables() {
        return documentosContables;
    }

    public void setDocumentosContables(Set<DocumentoContable> documentosContables) {
        this.documentosContables = documentosContables;
    }

    public Set<Sentencia> getSentencias() {
        return sentencias;
    }

    public void setSentencias(Set<Sentencia> sentencias) {
        this.sentencias = sentencias;
    }
}