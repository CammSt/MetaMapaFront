package ar.utn.da.dsi.frontend.client.dto.output;

import java.time.LocalDateTime;

public record SolicitudEliminacionOutputDTO(
		Long nroDeSolicitud,
		String nombreHecho,
		String tituloDelHechoAEliminar,
		String estado,
		String motivo,
		LocalDateTime fechaCreacionSolicitud
) {}
