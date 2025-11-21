package ar.utn.da.dsi.frontend.services.estadisticas;

import ar.utn.da.dsi.frontend.client.dto.output.CategoriaMasReportadaDTO;
import ar.utn.da.dsi.frontend.client.dto.output.ProvinciaMasHechosPorColeccionDTO;
import ar.utn.da.dsi.frontend.client.dto.output.ProvinciaHechosPorCategoriaDTO;
import ar.utn.da.dsi.frontend.client.dto.output.HoraHechosPorCategoriaDTO;
import ar.utn.da.dsi.frontend.client.dto.output.SolicitudSpamDTO;
import ar.utn.da.dsi.frontend.services.ApiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class EstadisticasApiClientService {

  private final ApiClientService apiClientService;
  private final String estadisticasApiUrl;

  @Autowired
  public EstadisticasApiClientService(ApiClientService apiClientService, @Value("${estadisticas.service.url}") String estadisticasApiUrl) {
    this.apiClientService = apiClientService;
    this.estadisticasApiUrl = estadisticasApiUrl;
  }
  // --- Nuevas Métricas Específicas (Corresponden a /estadisticas/* en el agregador) ---

  // 1) Provincia con más hechos por colección
  public ProvinciaMasHechosPorColeccionDTO getProvinciaConMasHechos(String handleId) {
    String url = estadisticasApiUrl + "/" + handleId + "/provincia-mas-hechos";
    return apiClientService.get(url, ProvinciaMasHechosPorColeccionDTO.class);
  }

  // 2) Categoría con más hechos
  public CategoriaMasReportadaDTO getCategoriaConMasHechos() {
    String url = estadisticasApiUrl + "/categoria-mas-reportada";
    return apiClientService.get(url, CategoriaMasReportadaDTO.class);
  }

  // 3) Provincia con más hechos por categoría
  public ProvinciaHechosPorCategoriaDTO getProvinciaConMasHechosSegunCategoria(String categoria) {
    String url = UriComponentsBuilder.fromHttpUrl(estadisticasApiUrl + "/provincia-mas-hechos-por-categoria")
        .queryParam("categoria", categoria)
        .toUriString();
    return apiClientService.get(url, ProvinciaHechosPorCategoriaDTO.class);
  }

  // 4) Hora con más hechos por categoría
  public HoraHechosPorCategoriaDTO getHoraConMasHechosSegunCategoria(String categoria) {
    String url = UriComponentsBuilder.fromHttpUrl(estadisticasApiUrl + "/hora-mas-hechos")
        .queryParam("categoria", categoria)
        .toUriString();
    return apiClientService.get(url, HoraHechosPorCategoriaDTO.class);
  }

  // 5) Cantidad de solicitudes spam
  public SolicitudSpamDTO getCantidadDeSolicitudesSpam() {
    String url = estadisticasApiUrl + "/solicitudes-spam";
    return apiClientService.get(url, SolicitudSpamDTO.class);
  }

  // --- Métodos de Exportación (Devuelven URL para descarga) ---

  // Exportar reporte completo (ZIP) - GET /estadisticas/exportar/todas
  public String getUrlExportarReporteCompletoZIP() {
    return estadisticasApiUrl + "/exportar/todas";
  }

  // 1) Exportar Provincia con más hechos por Colección (CSV) - GET /estadisticas/exportar/provincia_por_coleccion
  public String getUrlExportarProvinciaColeccion() {
    return estadisticasApiUrl + "/exportar/provincia_por_coleccion";
  }

  // 2) Exportar Categoría con más hechos (CSV) - GET /estadisticas/exportar/categoria_mas_hechos
  public String getUrlExportarCategoriasHechos() {
    return estadisticasApiUrl + "/exportar/categoria_mas_hechos";
  }

  // 3) Exportar Provincia con más hechos por Categoría (CSV) - GET /estadisticas/exportar/provincia_por_categoria
  public String getUrlExportarProvinciaCategoria() {
    return estadisticasApiUrl + "/exportar/provincia_por_categoria";
  }

  // 4) Exportar Hora con más hechos por Categoría (CSV) - GET /estadisticas/exportar/hora_por_categoria
  public String getUrlExportarHoraCategoria() {
    return estadisticasApiUrl + "/exportar/hora_por_categoria";
  }

  // 5) Exportar Cantidad de Solicitudes Spam (CSV) - GET /estadisticas/exportar/spam
  public String getUrlExportarSpam() {
    return estadisticasApiUrl + "/exportar/spam";
  }
}