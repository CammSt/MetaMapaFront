package ar.utn.da.dsi.frontend.client.dto.output;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EdicionOutputDTO {
	@JsonProperty("id")
	private Long id;

	@JsonProperty("idHechoOriginal")
	private Long idHechoOriginal;

	@JsonProperty("tituloPropuesto")
	private String tituloPropuesto;

	@JsonProperty("descripcionPropuesta")
	private String descripcionPropuesta;

	// CAMBIO: Recibimos los campos planos del backend
	@JsonProperty("categoriaPropuestaId")
	private Long categoriaPropuestaId;

	@JsonProperty("categoriaPropuestaNombre")
	private String categoriaPropuestaNombre;

	@JsonProperty("latitudPropuesta")
	private Double latitudPropuesta;

	@JsonProperty("longitudPropuesta")
	private Double longitudPropuesta;

	@JsonProperty("fechaAcontecimientoPropuesta")
	private LocalDateTime fechaAcontecimientoPropuesta;

	@JsonProperty("contenidoMultimediaPropuesto")
	private String contenidoMultimediaPropuesto;

	@JsonProperty("visualizadorEditor") // Puede venir como visualizadorEditor o visualizadorEditorId, checkea el JSON si falla
	private String visualizadorEditor;

	@JsonProperty("fechaEdicion")
	private LocalDate fechaEdicion;

	@JsonProperty("estado")
	private String estado;

	@JsonProperty("detalle")
	private String detalle;

	public String getNombreCategoria() {
		return categoriaPropuestaNombre != null ? categoriaPropuestaNombre : "";
	}
}