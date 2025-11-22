package ar.utn.da.dsi.frontend.services.estadisticas;

import ar.utn.da.dsi.frontend.client.dto.output.CategoriaMasReportadaDTO;
import ar.utn.da.dsi.frontend.client.dto.output.HoraHechosPorCategoriaDTO;
import ar.utn.da.dsi.frontend.client.dto.output.ProvinciaHechosPorCategoriaDTO;
import ar.utn.da.dsi.frontend.client.dto.output.ProvinciaMasHechosPorColeccionDTO;
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

  //1) Provincia con más hechos por colección
  public ProvinciaMasHechosPorColeccionDTO getProvinciaMasHechosPorColeccion(String handleId) {
    return apiClient.getProvinciaConMasHechos(handleId);
  }

  // 2) Categoría con más hechos
  public CategoriaMasReportadaDTO getCategoriaMasReportada() {
    return apiClient.getCategoriaConMasHechos();
  }

  // 3) Provincia con más hechos por categoría
  public ProvinciaHechosPorCategoriaDTO getProvinciaMasHechosPorCategoria(String categoria) {
    return apiClient.getProvinciaConMasHechosSegunCategoria(categoria);
  }

  // 4) Hora con más hechos por categoría
  public HoraHechosPorCategoriaDTO getHoraMasHechosPorCategoria(String categoria) {
    return apiClient.getHoraConMasHechosSegunCategoria(categoria);
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