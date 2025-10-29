package com.bufete.backend.Dtos.sentencia;

import java.time.LocalDate;

public class SentenciaDto {
    private Long id;
    private String nombreProceso;
    private String numeroExpediente;
    private LocalDate fechaSentencia;
    private String nombreAbogadoACargo;

    public SentenciaDto() {
    }

    public SentenciaDto(Long id, String nombreProceso, String numeroExpediente, 
                        LocalDate fechaSentencia, String nombreAbogadoACargo) {
        this.id = id;
        this.nombreProceso = nombreProceso;
        this.numeroExpediente = numeroExpediente;
        this.fechaSentencia = fechaSentencia;
        this.nombreAbogadoACargo = nombreAbogadoACargo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreProceso() {
        return nombreProceso;
    }

    public void setNombreProceso(String nombreProceso) {
        this.nombreProceso = nombreProceso;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public LocalDate getFechaSentencia() {
        return fechaSentencia;
    }

    public void setFechaSentencia(LocalDate fechaSentencia) {
        this.fechaSentencia = fechaSentencia;
    }

    public String getNombreAbogadoACargo() {
        return nombreAbogadoACargo;
    }

    public void setNombreAbogadoACargo(String nombreAbogadoACargo) {
        this.nombreAbogadoACargo = nombreAbogadoACargo;
    }
}
