package ar.utn.da.dsi.frontend.client.dto.output;

import java.util.List;

public class HoraHechosPorCategoriaListDTO {
  private String categoria; // Categoría consultada
  private List<HoraHechosData> distribucion; // Distribución de conteos para cada hora del día
}