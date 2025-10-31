package com.bufete.backend.utils;

import com.bufete.backend.Dtos.proceso.CreateProcesoRequest;
import com.bufete.backend.Dtos.proceso.ProcesoDTO;
import com.bufete.backend.model.Cliente;
import com.bufete.backend.model.Proceso;
import com.bufete.backend.model.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-31T11:19:48-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Oracle Corporation)"
)
@Component
public class ProcesoMapperImpl implements ProcesoMapper {

    @Override
    public ProcesoDTO toDTO(Proceso proceso) {
        if ( proceso == null ) {
            return null;
        }

        ProcesoDTO.ProcesoDTOBuilder procesoDTO = ProcesoDTO.builder();

        procesoDTO.clienteNombre( procesoClienteNombre( proceso ) );
        procesoDTO.createdByNombre( procesoCreatedByNombre( proceso ) );
        procesoDTO.id( proceso.getId() );
        procesoDTO.numeroProceso( proceso.getNumeroProceso() );
        procesoDTO.nombre( proceso.getNombre() );
        procesoDTO.descripcion( proceso.getDescripcion() );
        procesoDTO.tipoProceso( proceso.getTipoProceso() );
        procesoDTO.estado( proceso.getEstado() );
        procesoDTO.fechaCreacion( proceso.getFechaCreacion() );
        procesoDTO.fechaInicio( proceso.getFechaInicio() );
        procesoDTO.fechaCierre( proceso.getFechaCierre() );
        procesoDTO.juzgado( proceso.getJuzgado() );
        procesoDTO.radicado( proceso.getRadicado() );
        procesoDTO.demandante( proceso.getDemandante() );
        procesoDTO.demandado( proceso.getDemandado() );
        procesoDTO.cuantia( proceso.getCuantia() );
        procesoDTO.observaciones( proceso.getObservaciones() );
        procesoDTO.activo( proceso.getActivo() );
        procesoDTO.createdAt( proceso.getCreatedAt() );
        procesoDTO.updatedAt( proceso.getUpdatedAt() );

        procesoDTO.abogadoResponsableNombre( proceso.getAbogadoResponsable().getNombre() + " " + proceso.getAbogadoResponsable().getApellido() );

        return procesoDTO.build();
    }

    @Override
    public List<ProcesoDTO> toDTOList(List<Proceso> procesos) {
        if ( procesos == null ) {
            return null;
        }

        List<ProcesoDTO> list = new ArrayList<ProcesoDTO>( procesos.size() );
        for ( Proceso proceso : procesos ) {
            list.add( toDTO( proceso ) );
        }

        return list;
    }

    @Override
    public Proceso toEntity(CreateProcesoRequest request) {
        if ( request == null ) {
            return null;
        }

        Proceso.ProcesoBuilder proceso = Proceso.builder();

        proceso.numeroProceso( request.getNumeroProceso() );
        proceso.nombre( request.getNombre() );
        proceso.descripcion( request.getDescripcion() );
        proceso.tipoProceso( request.getTipoProceso() );
        proceso.fechaInicio( request.getFechaInicio() );
        proceso.juzgado( request.getJuzgado() );
        proceso.radicado( request.getRadicado() );
        proceso.demandante( request.getDemandante() );
        proceso.demandado( request.getDemandado() );
        proceso.cuantia( request.getCuantia() );
        proceso.observaciones( request.getObservaciones() );

        return proceso.build();
    }

    @Override
    public void updateEntity(Proceso proceso, CreateProcesoRequest request) {
        if ( request == null ) {
            return;
        }

        if ( request.getNumeroProceso() != null ) {
            proceso.setNumeroProceso( request.getNumeroProceso() );
        }
        if ( request.getNombre() != null ) {
            proceso.setNombre( request.getNombre() );
        }
        if ( request.getDescripcion() != null ) {
            proceso.setDescripcion( request.getDescripcion() );
        }
        if ( request.getTipoProceso() != null ) {
            proceso.setTipoProceso( request.getTipoProceso() );
        }
        if ( request.getFechaInicio() != null ) {
            proceso.setFechaInicio( request.getFechaInicio() );
        }
        if ( request.getJuzgado() != null ) {
            proceso.setJuzgado( request.getJuzgado() );
        }
        if ( request.getRadicado() != null ) {
            proceso.setRadicado( request.getRadicado() );
        }
        if ( request.getDemandante() != null ) {
            proceso.setDemandante( request.getDemandante() );
        }
        if ( request.getDemandado() != null ) {
            proceso.setDemandado( request.getDemandado() );
        }
        if ( request.getCuantia() != null ) {
            proceso.setCuantia( request.getCuantia() );
        }
        if ( request.getObservaciones() != null ) {
            proceso.setObservaciones( request.getObservaciones() );
        }
    }

    private String procesoClienteNombre(Proceso proceso) {
        if ( proceso == null ) {
            return null;
        }
        Cliente cliente = proceso.getCliente();
        if ( cliente == null ) {
            return null;
        }
        String nombre = cliente.getNombre();
        if ( nombre == null ) {
            return null;
        }
        return nombre;
    }

    private String procesoCreatedByNombre(Proceso proceso) {
        if ( proceso == null ) {
            return null;
        }
        Usuario createdBy = proceso.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        String nombre = createdBy.getNombre();
        if ( nombre == null ) {
            return null;
        }
        return nombre;
    }
}
