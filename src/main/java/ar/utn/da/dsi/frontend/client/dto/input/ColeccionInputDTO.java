package ar.utn.da.dsi.frontend.client.dto.input;

import lombok.Data;

@Data
public class ColeccionInputDTO {
  private String titulo;
  private String descripcion;
  private String visualizadorID;
  private String algoritmoConsenso;
  private String handleID;
}
