package ar.utn.da.dsi.frontend.client.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudUnificadaDTO {
	private Long id;
	private String tituloDelHechoAEliminar; // Mismo nombre que usa el HTML para no romper nada
	private String tipo;   // "Nuevo Hecho" o "Eliminación"
	private String estado; // "PENDIENTE", "APROBADA", etc.
}