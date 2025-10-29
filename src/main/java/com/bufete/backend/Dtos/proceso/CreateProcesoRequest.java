package com.bufete.backend.Dtos.proceso;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProcesoRequest {
    @NotBlank(message = "El número de proceso es obligatorio")
    private String numeroProceso;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    private String descripcion;
    private String tipoProceso;
    private LocalDate fechaInicio;
    
    private String tipoDocumentoCliente; 

    @NotNull(message = "El cliente es obligatorio")
    private String clienteId;
    
    @NotNull(message = "El abogado responsable es obligatorio")
    private Long abogadoResponsableId;
    
    private String juzgado;
    private String radicado;
    private String demandante;
    private String demandado;
    private BigDecimal cuantia;
    private String observaciones;
}