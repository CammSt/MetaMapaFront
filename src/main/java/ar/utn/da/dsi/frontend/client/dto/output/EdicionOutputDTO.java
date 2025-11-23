package ar.utn.da.dsi.frontend.client.dto.output;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class EdicionOutputDTO {
	private Long id;
	private Long idHechoOriginal;
	private String tituloPropuesto;
	private String descripcionPropuesta;

	// Mapeamos la categoría anidada para que no rompa
	private CategoriaDto categoriaPropuesta;

	private Double latitudPropuesta;
	private Double longitudPropuesta;
	private LocalDateTime fechaAcontecimientoPropuesta;
	private String contenidoMultimediaPropuesto;
	private String visualizadorEditor;
	private LocalDate fechaEdicion;
	private String estado; // PENDIENTE, APROBADA...
	private String detalle;

	// Helper para obtener el nombre directo en el HTML
	public String getNombreCategoria() {
		return categoriaPropuesta != null ? categoriaPropuesta.getNombre() : "";
	}

	@Data
	public static class CategoriaDto {
		private Long id;
		private String nombre;
	}
}