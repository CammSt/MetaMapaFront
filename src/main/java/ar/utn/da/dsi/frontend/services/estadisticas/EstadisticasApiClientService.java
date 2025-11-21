package ar.utn.da.dsi.frontend.services.estadisticas;

import ar.utn.da.dsi.frontend.client.dto.output.EstadisticasDTO;
import ar.utn.da.dsi.frontend.services.ApiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasApiClientService {

  private final ApiClientService apiClientService;
  private final String estadisticasApiUrl;

  @Autowired
  public EstadisticasApiClientService(ApiClientService apiClientService, @Value("${estadisticas.service.url}") String estadisticasApiUrl) {
    this.apiClientService = apiClientService;
    this.estadisticasApiUrl = estadisticasApiUrl;
  }

  /**
   * Obtiene las estadísticas agregadas para el panel de administración.
   * Endpoint en el backend: GET /estadisticas/admin/general
   */
  public EstadisticasDTO obtenerEstadisticasGenerales() {
    String url = estadisticasApiUrl + "/admin/general";
    return apiClientService.get(url, EstadisticasDTO.class);
  }
}