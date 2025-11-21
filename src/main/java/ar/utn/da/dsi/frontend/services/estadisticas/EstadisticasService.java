package ar.utn.da.dsi.frontend.services.estadisticas;

import ar.utn.da.dsi.frontend.client.dto.output.EstadisticasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasService {

  private final EstadisticasApiClientService apiClient;

  @Autowired
  public EstadisticasService(EstadisticasApiClientService apiClient) {
    this.apiClient = apiClient;
  }

  public EstadisticasDTO obtenerEstadisticasAdmin() {
    return apiClient.obtenerEstadisticasGenerales();
  }
}