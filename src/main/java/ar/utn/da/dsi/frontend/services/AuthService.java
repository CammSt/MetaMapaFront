package ar.utn.da.dsi.frontend.services;

import ar.utn.da.dsi.frontend.client.dto.AuthResponseDTO;
import ar.utn.da.dsi.frontend.client.dto.RolesPermisosDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class AuthService {

	private static final Logger log = LoggerFactory.getLogger(AuthService.class);
	private final WebClient webClient;
	private final ApiClientService apiClientService;
	private final String authServiceUrl;

	@Autowired
	public AuthService(
																	 ApiClientService apiClientService,
																	 @Value("${auth.service.url}") String authServiceUrl) {
		this.webClient = WebClient.builder().build();
		this.apiClientService = apiClientService;
		this.authServiceUrl = authServiceUrl;
	}

	/**
	 * Llama al endpoint de login del backend.
	 * Esta es la llamada que usa CustomAuthProvider.
	 */
	public AuthResponseDTO login(String username, String password) {
		try {
			AuthResponseDTO response = webClient
					.post()
					.uri(authServiceUrl + "/auth") // Asumiendo endpoint /auth
					.bodyValue(Map.of(
							"username", username,
							"password", password
					))
					.retrieve()
					.bodyToMono(AuthResponseDTO.class)
					.block();
			return response;
		} catch (WebClientResponseException e) {
			log.error(e.getMessage());
			if (e.getStatusCode() == HttpStatus.NOT_FOUND || e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				return null;
			}
			// Otros errores HTTP
			throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
		}
	}

	/**
	 * Llama al endpoint que devuelve roles y permisos.
	 * Esta es la llamada que usa CustomAuthProvider.
	 */
	public RolesPermisosDTO getRolesPermisos(String accessToken) {
		try {
			// Usamos el método 'getWithAuth' de ApiClientService
			// que NO intenta refrescar el token (porque aún no lo tenemos del todo)
			RolesPermisosDTO response = apiClientService.getWithAuth(
					authServiceUrl + "/auth/user/roles-permisos", // Asumiendo endpoint
					accessToken,
					RolesPermisosDTO.class
			);
			return response;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException("Error al obtener roles y permisos: " + e.getMessage(), e);
		}
	}

	// (Hemos eliminado todo el código comentado de "Alumnos")
}