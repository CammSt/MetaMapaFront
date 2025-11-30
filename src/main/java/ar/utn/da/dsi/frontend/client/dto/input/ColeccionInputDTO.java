package ar.utn.da.dsi.frontend.client.dto.input;

import lombok.Data;
import java.util.List;

@Data
public class ColeccionInputDTO {
  private String titulo;
  private String descripcion;
  private String visualizadorID;
  private String handleID;
  private List<String> fuentes;
  private String algoritmoConsenso;
  private List<String> criteriosPertenenciaNombres;
  private List<String> criteriosPertenenciaValores;
  private List<Long> idDeHechosParaEliminar;
}
