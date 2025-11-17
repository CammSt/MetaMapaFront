package ar.utn.da.dsi.frontend.services.hechos;

import ar.utn.da.dsi.frontend.client.dto.HechoDTO;
import ar.utn.da.dsi.frontend.client.dto.input.HechoInputDTO;
import ar.utn.da.dsi.frontend.services.ApiClientService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class HechoApiService {

  private final ApiClientService apiClientService;
  private final String hechosApiUrl;
  private final WebClient webClient;

  @Autowired
  public HechoApiService(ApiClientService apiClientService, @Value("${hechos.service.url}") String hechosApiUrl) {
    this.apiClientService = apiClientService;
    this.hechosApiUrl = hechosApiUrl;
    this.webClient = WebClient.builder().build();
  }

  /**
   * Llama al backend para obtener los hechos de una colección.
   * (Usado por WebController para la página de /facts)
   */
  public List<HechoDTO> getHechosDeColeccion(String handleId) {
    // Asumo que el endpoint del backend es /hechos/coleccion/{handleId}
    // Si es diferente, ajustamos esta URL.
    String url = hechosApiUrl + "/coleccion/" + handleId;
    return apiClientService.getList(url, HechoDTO.class);
  }

  /**
   * Llama al backend para obtener los hechos de un usuario.
   * (Usado por ContributorController para el panel "Mis Hechos")
   */
  public List<HechoDTO> getHechosPorUsuario(String userId) {
    // Asumo que el endpoint del backend es /hechos/usuario/{userId}
    String url = hechosApiUrl + "/usuario/" + userId;
    return apiClientService.getList(url, HechoDTO.class);
  }

  /**
   * Llama al backend para obtener las categorías de hechos disponibles.
   * (Usado por WebController para los filtros en la página /facts)
   */
  public List<String> getAvailableCategories() {
    // Asumo que el endpoint del backend es /hechos/categorias
    String url = hechosApiUrl + "/categorias";
    return apiClientService.getList(url, String.class);
  }

  public Map<String, String> getConsensusLabels() {
    // Asumo que el endpoint del backend es /hechos/metadata/consensus
    String url = hechosApiUrl + "/metadata/consensus";
    // NOTA: Map.class es complicado para WebClient,
    // si esto falla, necesitaremos un DTO. Por ahora lo intentamos así.
    return apiClientService.get(url, Map.class);
  }

  public List<String> getAvailableSources() {
    // Asumo que el endpoint del backend es /hechos/metadata/sources
    String url = hechosApiUrl + "/metadata/sources";
    return apiClientService.getList(url, String.class);
  }

  public HechoDTO getHechoPorId(Long id) {
    String url = hechosApiUrl + "/" + id;
    return apiClientService.get(url, HechoDTO.class);
  }

  public HechoDTO crearHecho(HechoInputDTO dto) {
    // Asumimos que el endpoint de creación es la URL base
    return apiClientService.post(hechosApiUrl, dto, HechoDTO.class);
  }

  public HechoDTO actualizarHecho(Long id, HechoInputDTO dto) {
    String url = hechosApiUrl + "/" + id;
    return apiClientService.put(url, dto, HechoDTO.class);
  }

  //Envía un POST multipart/form-data para importar un CSV
  public void importarCsv(MultipartFile file) {
    String url = hechosApiUrl + "/importar-csv";

    // Es una llamada "especial", no podemos usar executeWithTokenRetry
    // Pero SÍ podemos usar el ApiClientService para obtener el token actual
    String accessToken = apiClientService.getAccessTokenFromSession();
    if (accessToken == null) {
      throw new RuntimeException("No hay token de acceso disponible para la subida");
    }

    try {
      webClient.post()
          .uri(url)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
          .contentType(MediaType.MULTIPART_FORM_DATA)
          .body(BodyInserters.fromMultipartData("file", file.getResource()))
          .retrieve()
          .bodyToMono(Void.class)
          .block();
    } catch (Exception e) {
      throw new RuntimeException("Error al importar CSV: " + e.getMessage(), e);
    }
  }
}
