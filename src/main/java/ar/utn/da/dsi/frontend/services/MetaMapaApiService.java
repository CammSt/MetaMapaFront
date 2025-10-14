package ar.utn.da.dsi.frontend.services;

import ar.utn.da.dsi.frontend.models.Coleccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

/**
 * CAPA INTERMEDIA (Equivalente a GestionAlumnosApiService)
 * Conoce las URLs y la estructura de la API del backend de MetaMapa.
 */
@Service
public class MetaMapaApiService {

  @Autowired
  private ApiClientService apiClientService; // Inyectamos el "mensajero"

  @Value("${metamapa.api.url}") // URL configurada en application.properties
  private String apiUrl;

  public List<Coleccion> obtenerTodasLasColecciones() throws IOException {
    String url = apiUrl + "/colecciones";
    // Delega la ejecución de la llamada GET al servicio genérico
    return apiClientService.getList(url, Coleccion.class);
  }

  public Coleccion crearColeccion(Coleccion nuevaColeccion) throws IOException {
    String url = apiUrl + "/colecciones";
    // Delega la ejecución de la llamada POST al servicio genérico
    return apiClientService.post(url, nuevaColeccion, Coleccion.class);
  }
}