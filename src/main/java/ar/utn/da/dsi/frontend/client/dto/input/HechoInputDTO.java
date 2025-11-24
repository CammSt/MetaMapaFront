package ar.utn.da.dsi.frontend.client.dto.input;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
public class HechoInputDTO {
  private Long id;
  private String titulo;
  private String descripcion;
  private String categoria;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime fechaAcontecimiento;

  private double latitud;
  private double longitud;
  private String collectionHandle;
  private String visualizadorID;
}