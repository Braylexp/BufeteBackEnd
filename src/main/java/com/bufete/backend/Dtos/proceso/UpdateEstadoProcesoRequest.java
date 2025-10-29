package com.bufete.backend.Dtos.proceso;

import com.bufete.backend.model.Proceso;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UpdateEstadoProcesoRequest {
    @NotNull(message = "El estado es obligatorio")
    private Proceso.EstadoProceso estado;

    public Proceso.EstadoProceso getEstado() {
        return estado;
    }

    public void setEstado(Proceso.EstadoProceso estado) {
        this.estado = estado;
    }

    
}