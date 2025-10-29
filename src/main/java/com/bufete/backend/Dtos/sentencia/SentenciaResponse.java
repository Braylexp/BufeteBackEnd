package com.bufete.backend.Dtos.sentencia;

import java.time.LocalDate;

public record SentenciaResponse(
    Long id,
    String nombre,
    String tipoSentencia,
    LocalDate fechaSentencia,
    String procesoId,
    String clienteId,
    String abogadoId,
    String fileBlobId,
    Long createdById
) {}
