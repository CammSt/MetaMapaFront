package ar.utn.da.dsi.frontend.client.dto.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SolicitudEliminacionOutputDTO(
		@JsonProperty("nroDeSolicitud") Long nroDeSolicitud, // Mapeamos 'id' del JSON a 'nroDeSolicitud'
		@JsonProperty("nombreHecho") String nombreHecho,
		@JsonProperty("estado") String estado,
		@JsonProperty("motivo") String motivo,
		@JsonProperty("fechaCreacionSolicitud") LocalDateTime fechaCreacionSolicitud
) {}