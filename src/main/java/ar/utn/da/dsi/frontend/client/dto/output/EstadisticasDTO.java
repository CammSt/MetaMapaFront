package ar.utn.da.dsi.frontend.client.dto.output;

import lombok.Data;

@Data
public class EstadisticasDTO {
  private Long totalHechos;
  private Long hechosPendientes;
  private Long solicitudesPendientes;
  private Long totalContribuyentes;
  private Long totalColecciones;
}