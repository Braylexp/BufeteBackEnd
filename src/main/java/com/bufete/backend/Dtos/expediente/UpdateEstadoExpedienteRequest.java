package com.bufete.backend.Dtos.expediente;

import com.bufete.backend.model.Expediente;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UpdateEstadoExpedienteRequest {
    @NotNull(message = "El estado es obligatorio")
    private Expediente.EstadoExpediente estado;

    public Expediente.EstadoExpediente getEstado() {
        return estado;
    }

    public void setEstado(Expediente.EstadoExpediente estado) {
        this.estado = estado;
    }
}