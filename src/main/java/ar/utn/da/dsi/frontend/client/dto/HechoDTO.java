package ar.utn.da.dsi.frontend.client.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record HechoDTO(
		Long id,
		Long userId,
		String collectionHandle,
		String titulo,
		String descripcion,
		String categoria,
		LocalDateTime fechaAcontecimiento,
		LocalDate fechaCarga,
		double latitud,
		double longitud,
		boolean consensuado,
		String estado
) {}