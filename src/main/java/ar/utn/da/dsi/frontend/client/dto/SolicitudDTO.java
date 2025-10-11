package ar.utn.da.dsi.frontend.client.dto;

public record SolicitudDTO(
		Long nroDeSolicitud,
		Long userId, // Añadido para poder filtrar
		String tituloDelHechoAEliminar,
		String estado,
		String motivo,
		String tipo // Añadido para poder filtrar
) {}