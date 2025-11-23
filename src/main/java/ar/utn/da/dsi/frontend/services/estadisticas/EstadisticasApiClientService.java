package ar.utn.da.dsi.frontend.services.estadisticas;

import ar.utn.da.dsi.frontend.client.dto.output.CategoriaReportadaListDTO;
import ar.utn.da.dsi.frontend.client.dto.output.ProvinciaHechosPorColeccionListDTO;
import ar.utn.da.dsi.frontend.client.dto.output.ProvinciaHechosPorCategoriaListDTO;
import ar.utn.da.dsi.frontend.client.dto.output.HoraHechosPorCategoriaListDTO;
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

  // Métrica 1: Distribución de provincias por colección
  // GET /estadisticas/{handleId}/distribucion-provincias
  public ProvinciaHechosPorColeccionListDTO getDistribucionProvincias(String handleId) {
    String url = estadisticasApiUrl + "/" + handleId + "/distribucion-provincias";
    return apiClientService.get(url, ProvinciaHechosPorColeccionListDTO.class);
  }

  // Métrica 2: Distribución de categorías global
  // GET /estadisticas/distribucion-categorias
  public CategoriaReportadaListDTO getDistribucionCategorias() {
    String url = estadisticasApiUrl + "/distribucion-categorias";
    return apiClientService.get(url, CategoriaReportadaListDTO.class);
  }

  // Métrica 3: Distribución de provincias por categoría
  // GET /estadisticas/distribucion-provincias-por-categoria?categoria={nombre}
  public ProvinciaHechosPorCategoriaListDTO getDistribucionProvinciasPorCategoria(String categoria) {
    String url = UriComponentsBuilder.fromHttpUrl(estadisticasApiUrl + "/distribucion-provincias-por-categoria")
        .queryParam("categoria", categoria)
        .toUriString();
    return apiClientService.get(url, ProvinciaHechosPorCategoriaListDTO.class);
  }

  // Métrica 4: Distribución de horas por categoría
  // GET /estadisticas/distribucion-horas-por-categoria?categoria={nombre}
  public HoraHechosPorCategoriaListDTO getDistribucionHorasPorCategoria(String categoria) {
    String url = UriComponentsBuilder.fromHttpUrl(estadisticasApiUrl + "/distribucion-horas-por-categoria")
        .queryParam("categoria", categoria)
        .toUriString();
    return apiClientService.get(url, HoraHechosPorCategoriaListDTO.class);
  }

  // Métrica 5: Ratio de solicitudes de spam
  // GET /estadisticas/solicitudes-spam-ratio
  public SolicitudSpamDTO getSolicitudesSpamRatio() {
    String url = estadisticasApiUrl + "/solicitudes-spam-ratio";
    return apiClientService.get(url, SolicitudSpamDTO.class);
  }

  // --- Métodos de Exportación (Devuelven URL para descarga) ---

  private String getBaseUrlSinPrefijoEstadisticas() {
    if (estadisticasApiUrl.endsWith("/estadisticas")) {
      // Retorna "http://localhost:8085"
      return estadisticasApiUrl.substring(0, estadisticasApiUrl.length() - "/estadisticas".length());
    }
    return estadisticasApiUrl;
  }

  // Exportar reporte completo (ZIP) - GET /exportar/zip/todas
  public String getUrlExportarReporteCompletoZIP() {
    // Resulta en: http://localhost:8085/exportar/zip/todas
    return getBaseUrlSinPrefijoEstadisticas() + "/exportar/zip/todas";
  }

  // 1) Exportar Provincia por Colección (CSV) - GET /exportar/csv/provincia-por-coleccion
  public String getUrlExportarProvinciaColeccion() {
    return getBaseUrlSinPrefijoEstadisticas() + "/exportar/csv/provincia-por-coleccion";
  }

  // 2) Exportar Categoría Global (CSV) - GET /exportar/csv/categoria-mas-hechos
  public String getUrlExportarCategoriasHechos() {
    return getBaseUrlSinPrefijoEstadisticas() + "/exportar/csv/categoria-mas-hechos";
  }

  // 3) Exportar Provincia por Categoría (CSV) - GET /exportar/csv/provincia-por-categoria
  public String getUrlExportarProvinciaCategoria() {
    return getBaseUrlSinPrefijoEstadisticas() + "/exportar/csv/provincia-por-categoria";
  }

  // 4) Exportar Hora por Categoría (CSV) - GET /exportar/csv/hora-por-categoria
  public String getUrlExportarHoraCategoria() {
    return getBaseUrlSinPrefijoEstadisticas() + "/exportar/csv/hora-por-categoria";
  }

  // 5) Exportar Solicitudes Spam (CSV) - GET /exportar/csv/spam
  public String getUrlExportarSpam() {
    return getBaseUrlSinPrefijoEstadisticas() + "/exportar/csv/spam";
  }
}