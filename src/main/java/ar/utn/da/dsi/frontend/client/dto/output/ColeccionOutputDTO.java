package ar.utn.da.dsi.frontend.client.dto.output;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColeccionOutputDTO {
	private String titulo;
	private String handleID;
	private String descripcion;
	private String algoritmoConsenso;
	private List<String> fuentes;
	private List<String> criterios;
}