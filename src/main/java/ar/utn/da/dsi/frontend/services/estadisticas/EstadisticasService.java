package ar.utn.da.dsi.frontend.services.estadisticas;

import ar.utn.da.dsi.frontend.client.dto.output.CategoriaMasReportadaDTO;
import ar.utn.da.dsi.frontend.client.dto.output.SolicitudSpamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstadisticasService {

  private final EstadisticasApiClientService apiClient;

  @Autowired
  public EstadisticasService(EstadisticasApiClientService apiClient) {
    this.apiClient = apiClient;
  }

  // 2) Categoría con más hechos
  public CategoriaMasReportadaDTO getCategoriaMasReportada() {
    return apiClient.getCategoriaConMasHechos();
  }

  // 5) Cantidad de solicitudes spam
  public SolicitudSpamDTO getCantidadDeSolicitudesSpam() {
    return apiClient.getCantidadDeSolicitudesSpam();
  }

  // --- Métodos para la exportación (Devuelven URLs) ---
  public String getExportUrlZipCompleto() {
    return apiClient.getUrlExportarReporteCompletoZIP();
  }
  public String getExportUrlProvinciaColeccion() {
    return apiClient.getUrlExportarProvinciaColeccion();
  }
  public String getExportUrlCategoriaHechos() {
    return apiClient.getUrlExportarCategoriasHechos();
  }
  public String getExportUrlProvinciaCategoria() {
    return apiClient.getUrlExportarProvinciaCategoria();
  }
  public String getExportUrlHoraCategoria() {
    return apiClient.getUrlExportarHoraCategoria();
  }
  public String getExportUrlSpam() {
    return apiClient.getUrlExportarSpam();
  }
}