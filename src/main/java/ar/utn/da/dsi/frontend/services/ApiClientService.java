package ar.utn.da.dsi.frontend.services;

import ar.utn.da.dsi.frontend.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.util.List;

@Service
public class ApiClientService {

  @Getter
  private final WebClient webClient;

  public ApiClientService() {
    this.webClient = WebClient.builder().build();
  }

  /**
   * Ejecuta una llamada al API con el token de sesión.
   */
  public <T> T executeWithToken(ApiCall<T> apiCall) {
    String accessToken = getAccessTokenFromSession();

    /*if (accessToken == null) {
      throw new RuntimeException("No hay token de acceso disponible");
    }*/

    try {
      // Intento con el token actual
      return apiCall.execute(accessToken);
    } catch (WebClientResponseException e) {
      // Si falla por 401/403, simplemente relanza la excepción.
      // El usuario tendrá que volver a loguearse.
      if(e.getStatusCode() == HttpStatus.NOT_FOUND){
        throw new NotFoundException(e.getMessage());
      }
      throw new RuntimeException("Error en llamada al API: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio: " + e.getMessage(), e);
    }
  }

  /**
   * Ejecuta una llamada HTTP GET (Autenticada)
   */
  public <T> T get(String url, Class<T> responseType) {
    return executeWithToken(accessToken ->
        webClient
            .get()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(responseType)
            .block()
    );
  }

  /**
   * Ejecuta una llamada HTTP GET que retorna una lista (Autenticada)
   */
  public <T> List<T> getList(String url, Class<T> responseType) {
    return executeWithToken(accessToken ->
        webClient
            .get()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToFlux(responseType)
            .collectList()
            .block()
    );
  }

  /**
   * Ejecuta una llamada HTTP GET con un token específico (usado por AuthProvider)
   */
  public <T> T getWithAuth(String url, String accessToken, Class<T> responseType) {
    try {
      return webClient
          .get()
          .uri(url)
          .header("Authorization", "Bearer " + accessToken)
          .retrieve()
          .bodyToMono(responseType)
          .block();
    } catch (Exception e) {
      throw new RuntimeException("Error en llamada al API: " + e.getMessage(), e);
    }
  }

  /**
   * Ejecuta una llamada HTTP POST (Autenticada)
   */
  public <T> T post(String url, Object body, Class<T> responseType) {
    return executeWithToken(accessToken ->
        webClient
            .post()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(responseType)
            .block()
    );
  }

  /**
   * Ejecuta una llamada HTTP POST pública (sin token)
   */
  public <T> T postPublic(String url, Object body, Class<T> responseType) {
    try {
      return webClient
          .post()
          .uri(url)
          .bodyValue(body)
          .retrieve()
          .bodyToMono(responseType)
          .block();
    } catch (WebClientResponseException e) {
      if(e.getStatusCode() == HttpStatus.NOT_FOUND){
        throw new NotFoundException(e.getMessage());
      }
      throw new RuntimeException("Error en llamada al API pública: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio: " + e.getMessage(), e);
    }
  }

  /**
   * Ejecuta una llamada HTTP PUT (Autenticada)
   */
  public <T> T put(String url, Object body, Class<T> responseType) {
    return executeWithToken(accessToken ->
        webClient
            .put()
            .uri(url)
            .header("Authorization", "Bearer " + accessToken)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(responseType)
            .block()
    );
  }

  /**
   * Ejecuta una llamada HTTP DELETE (Autenticada)
   */
  public void delete(String url) {
    executeWithToken(accessToken -> {
      webClient
          .delete()
          .uri(url)
          .header("Authorization", "Bearer " + accessToken)
          .retrieve()
          .bodyToMono(Void.class)
          .block();
      return null;
    });
  }

  /**
   * Ejecuta una llamada HTTP GET pública (sin token)
   */
  public <T> T getPublic(String url, Class<T> responseType) {
    try {
      return webClient
          .get()
          .uri(url)
          .retrieve()
          .bodyToMono(responseType)
          .block();
    } catch (WebClientResponseException e) {
      if(e.getStatusCode() == HttpStatus.NOT_FOUND){
        throw new NotFoundException(e.getMessage());
      }
      throw new RuntimeException("Error en llamada al API pública: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio: " + e.getMessage(), e);
    }
  }

  /**
   * Ejecuta una llamada HTTP GET pública que retorna una lista (sin token)
   */
  public <T> List<T> getListPublic(String url, Class<T> responseType) {
    try {
      return webClient
          .get()
          .uri(url)
          .retrieve()
          .bodyToFlux(responseType)
          .collectList()
          .block();
    } catch (WebClientResponseException e) {
      if(e.getStatusCode() == HttpStatus.NOT_FOUND){
        throw new NotFoundException(e.getMessage());
      }
      throw new RuntimeException("Error en llamada al API pública: " + e.getMessage(), e);
    } catch (Exception e) {
      throw new RuntimeException("Error de conexión con el servicio: " + e.getMessage(), e);
    }
  }

  /**
   * Obtiene el access token de la sesión
   */
  public String getAccessTokenFromSession() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    return (String) request.getSession().getAttribute("accessToken");
  }

  @FunctionalInterface
  public interface ApiCall<T> {
    T execute(String accessToken) throws Exception;
  }
}