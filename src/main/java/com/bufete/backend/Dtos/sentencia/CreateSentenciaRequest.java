package com.bufete.backend.Dtos.sentencia;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public class CreateSentenciaRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "El tipo de sentencia es obligatorio")
    private String tipoSentencia;
    
    @NotNull(message = "La fecha de sentencia es obligatoria")
    private LocalDate fechaSentencia;
    
    @NotNull(message = "El ID del proceso es obligatorio")
    private Long procesoId;
    
    @NotNull(message = "El ID del cliente es obligatorio")
    private String clienteId;
    
    @NotNull(message = "El ID del abogado es obligatorio")
    private Long abogadoId;
    
    @NotNull(message = "El ID del archivo es obligatorio")
    private UUID fileBlobId;

    public CreateSentenciaRequest() {
    }

    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Long getProcesoId() {
        return procesoId;
    }

    public void setProcesoId(Long procesoId) {
        this.procesoId = procesoId;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public Long getAbogadoId() {
        return abogadoId;
    }

    public void setAbogadoId(Long abogadoId) {
        this.abogadoId = abogadoId;
    }

    public UUID getFileBlobId() {
        return fileBlobId;
    }

    public void setFileBlobId(UUID fileBlobId) {
        this.fileBlobId = fileBlobId;
    }
}
