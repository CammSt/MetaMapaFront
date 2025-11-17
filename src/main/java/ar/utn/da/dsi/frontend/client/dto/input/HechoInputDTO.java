package ar.utn.da.dsi.frontend.client.dto.input;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HechoInputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;
  private LocalDateTime fechaAcontecimiento;
  private double latitud;
  private double longitud;
  private String collectionHandle;
  private String visualizadorID;
}