package ar.utn.da.dsi.frontend.client.dto.output;

import java.util.List;

public class ProvinciaHechosPorCategoriaListDTO {
  private String categoria; // Categoría consultada
  private List<ProvinciaHechosData> distribucion; // Lista de distribución de hechos por provincia para esa categoría
}
