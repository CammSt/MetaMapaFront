package ar.utn.da.dsi.frontend.services.hechos;

import ar.utn.da.dsi.frontend.client.dto.HechoDTO;
import ar.utn.da.dsi.frontend.client.dto.input.HechoInputDTO;
import ar.utn.da.dsi.frontend.services.ApiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.lang.Nullable;
import ar.utn.da.dsi.frontend.client.dto.ApiResponse;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
public class HechoApiService {

  private final ApiClientService apiClientService;
  private final String hechosApiUrl;
  private final String estaticaApiUrl;
  private final WebClient webClient;
  private final ObjectMapper objectMapper; // AÑADIDO
  private final String agregadorApiUrl;

  @Autowired
  public HechoApiService(ApiClientService apiClientService, @Value("${dinamica.service.url}") String hechosApiUrl, @Value("${estatica.service.url}") String estaticaApiUrl, @Value("${agregador.service.url}") String agregadorApiUrl) {
    this.apiClientService = apiClientService;
    this.hechosApiUrl = hechosApiUrl;
    this.estaticaApiUrl = estaticaApiUrl;
    this.webClient = WebClient.builder().build();
    this.objectMapper = new ObjectMapper();
    this.objectMapper.findAndRegisterModules();
    this.agregadorApiUrl = agregadorApiUrl;
  }

  /**
   * Llama al backend para obtener los hechos de una colección.
   * (Usado por WebController para la página de /facts)
   */
  public List<HechoDTO> getHechosDeColeccion(String handleId, String modo, String fechaDesde, String fechaHasta, String categoria, String titulo) {

    String urlBase = hechosApiUrl + "/coleccion/" + handleId;

    // Usamos UriComponentsBuilder para añadir parámetros solo si no son nulos
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(urlBase);

    if (modo != null && !modo.isEmpty()) {
      builder.queryParam("modo", modo);
    }
    if (fechaDesde != null && !fechaDesde.isEmpty()) {
      builder.queryParam("fechaDesde", fechaDesde);
    }
    if (fechaHasta != null && !fechaHasta.isEmpty()) {
      builder.queryParam("fechaHasta", fechaHasta);
    }
    if (categoria != null && !categoria.isEmpty()) {
      builder.queryParam("categoria", categoria);
    }
    if (titulo != null && !titulo.isEmpty()) {
      builder.queryParam("titulo", titulo);
    }

    String urlCompleta = builder.toUriString();

    return apiClientService.getListPublic(urlCompleta, HechoDTO.class);
  }

  /**
   * Llama al backend para obtener los hechos de un usuario.
   * (Usado por ContributorController para el panel "Mis Hechos")
   */
  public List<HechoDTO> getHechosPorUsuario(String userId) {
    String url = hechosApiUrl + "/hechos/usuario/" + userId;

    // USAMOS executeWithToken PARA INYECTAR EL TOKEN AUTOMÁTICAMENTE
    return apiClientService.executeWithToken(accessToken ->
        webClient.get()
            .uri(url)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            // ACÁ ESTÁ LA MAGIA: Le decimos que esperamos un ApiResponse que contiene una Lista
            .bodyToMono(new ParameterizedTypeReference<ApiResponse<List<HechoDTO>>>() {})
            .map(response -> {
              if (response.getDatos() == null) return List.<HechoDTO>of();
              return response.getDatos();
            })
            .block()
    );
  }

  /**
   * Llama al backend para obtener las categorías de hechos disponibles.
   * (Usado por WebController para los filtros en la página /facts)
   */
  public List<String> getAvailableCategories() {
    // El endpoint del agregador es /hechos/categorias
    String url = agregadorApiUrl + "/hechos/categorias";
    String[] categoriasArray = apiClientService.getPublic(url, String[].class);

    if (categoriasArray == null) {
      return List.of();
    }

    return Arrays.asList(categoriasArray);
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

  /**
   * CREAR HECHO DINÁMICO: Envía la solicitud como Multipart (JSON + Archivo).
   * Soporta usuarios logueados (con token) y anónimos (sin token).
   */
  public HechoDTO crearHecho(HechoInputDTO dto, @Nullable MultipartFile archivo) {
    String url = hechosApiUrl + "/hechos";

    try {
      String hechoJson = objectMapper.writeValueAsString(dto);

      MultipartBodyBuilder builder = new MultipartBodyBuilder();
      builder.part("hechoData", hechoJson, MediaType.APPLICATION_JSON);

      if (archivo != null && !archivo.isEmpty()) {
        builder.part("archivo", archivo.getResource());
      }

      // 1. Intentamos obtener el token de la sesión
      String accessToken = apiClientService.getAccessTokenFromSession();

      // 2. Configuramos el WebClient base (POST y Body)
      var requestSpec = webClient.post()
          .uri(url)
          .contentType(MediaType.MULTIPART_FORM_DATA) // Importante para multipart
          .body(BodyInserters.fromMultipartData(builder.build()));

      // 3. Si hay token, agregamos el Header. Si no, va sin header.
      if (accessToken != null && !accessToken.isBlank()) {
        requestSpec.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
      }

      // 4. Ejecutamos
      return requestSpec
          .retrieve()
          .bodyToMono(HechoDTO.class)
          .block();

    } catch (Exception e) {
      throw new RuntimeException("Error al crear Hecho en Fuente Dinámica: " + e.getMessage(), e);
    }
  }

  /**
   * ACTUALIZAR HECHO DINÁMICO: Envía la solicitud de edición como Multipart (JSON + Archivo) al endpoint /hechos/{id}/editar.
   */
  public HechoDTO actualizarHecho(Long id, HechoInputDTO dto, @Nullable MultipartFile archivo) {
    String url = hechosApiUrl + "/hechos/" + id + "/editar"; // Endpoint: PUT /hechos/{id}/editar

    // Mapeamos HechoInputDTO a la estructura necesaria para EdicionInputDTO (solo los campos propuestos)
    Map<String, Object> edicionData = new LinkedHashMap<>();
    edicionData.put("tituloPropuesto", dto.getTitulo());
    edicionData.put("descripcionPropuesta", dto.getDescripcion());
    edicionData.put("categoriaPropuesta", Map.of("nombre", dto.getCategoria())); // El backend espera un objeto Categoria
    edicionData.put("latitudPropuesta", dto.getLatitud());
    edicionData.put("longitudPropuesta", dto.getLongitud());
    edicionData.put("fechaAcontecimientoPropuesta", dto.getFechaAcontecimiento());

    try {
      String edicionJson = objectMapper.writeValueAsString(edicionData);

      MultipartBodyBuilder builder = new MultipartBodyBuilder();
      // Parte JSON: "edicionData" con Content-Type application/json
      builder.part("edicionData", edicionJson, MediaType.APPLICATION_JSON);

      // Parte Archivo: "archivo" si existe
      if (archivo != null && !archivo.isEmpty()) {
        builder.part("archivo", archivo.getResource());
      }

      return apiClientService.executeWithToken(accessToken ->
          webClient.put()
              .uri(url)
              .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
              .body(BodyInserters.fromMultipartData(builder.build()))
              .retrieve()
              .bodyToMono(HechoDTO.class)
              .block()
      );

    } catch (Exception e) {
      throw new RuntimeException("Error al actualizar Hecho en Fuente Dinámica: " + e.getMessage(), e);
    }
  }

  //Envía un POST multipart/form-data para importar un CSV
  public void importarCsv(MultipartFile file) {
    String url = estaticaApiUrl + "/cargar-csv";

    String accessToken = apiClientService.getAccessTokenFromSession();
    if (accessToken == null) {
      throw new RuntimeException("No hay token de acceso disponible para la subida");
    }

    try {
      webClient.post()
          .uri(url)
          .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
          .contentType(MediaType.MULTIPART_FORM_DATA)
          .body(BodyInserters.fromMultipartData("archivoCSV", file.getResource()))
          .retrieve()
          .bodyToMono(Void.class)
          .block();
    } catch (Exception e) {
      throw new RuntimeException("Error al importar CSV: " + e.getMessage(), e);
    }
  }
}