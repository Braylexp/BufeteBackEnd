package com.bufete.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tipo_evento")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombre", unique = true, nullable = false, length = 50)
    private String nombre;
    
    @Column(name = "color", length = 7)
    @Builder.Default
    private String color = "#3498db";
    
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;
    
    @OneToMany(mappedBy = "tipoEvento")
    @Builder.Default
    private Set<Evento> eventos = new HashSet<>();

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(Set<Evento> eventos) {
        this.eventos = eventos;
    }

    
}