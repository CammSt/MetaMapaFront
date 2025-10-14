package ar.utn.da.dsi.frontend.services;

import ar.utn.da.dsi.frontend.client.dto.AuthResponseDTO;
import ar.utn.da.dsi.frontend.client.dto.RolesPermisosDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class AuthService {

	private static final Logger log = LoggerFactory.getLogger(GestionAlumnosApiService.class);
	private final WebClient webClient;
	private final ApiClientService apiClientService;
	private final String authServiceUrl;
	private final String alumnosServiceUrl;

	@Autowired
	public GestionAlumnosApiService(
			ApiClientService apiClientService,
			@Value("${auth.service.url}") String authServiceUrl,
			@Value("${alumnos.service.url}") String alumnosServiceUrl) {
		this.webClient = WebClient.builder().build();
		this.apiClientService = apiClientService;
		this.authServiceUrl = authServiceUrl;
		this.alumnosServiceUrl = alumnosServiceUrl;
	}

	public AuthResponseDTO login(String username, String password) {
		try {
			AuthResponseDTO response = webClient
					.post()
					.uri(authServiceUrl + "/auth")
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
			if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
				// Login fallido - credenciales incorrectas
				return null;
			}
			// Otros errores HTTP
			throw new RuntimeException("Error en el servicio de autenticación: " + e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException("Error de conexión con el servicio de autenticación: " + e.getMessage(), e);
		}
	}

	public RolesPermisosDTO getRolesPermisos(String accessToken) {
		try {
			RolesPermisosDTO response = webApiCallerService.getWithAuth(
					authServiceUrl + "/auth/user/roles-permisos",
					accessToken,
					RolesPermisosDTO.class
			);
			return response;
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new RuntimeException("Error al obtener roles y permisos: " + e.getMessage(), e);
		}
	}

/*	public List<AlumnoDTO> obtenerTodosLosAlumnos() {
		List<AlumnoDTO> response = webApiCallerService.getList(alumnosServiceUrl + "/alumnos", AlumnoDTO.class);
		return response != null ? response : List.of();
	}

	public AlumnoDTO obtenerAlumnoPorLegajo(String legajo) {
		AlumnoDTO response = webApiCallerService.get(alumnosServiceUrl + "/alumnos/" + legajo, AlumnoDTO.class);
		if (response == null) {
			throw new NotFoundException("Alumno", legajo);
		}
		return response;
	}

	public AlumnoDTO crearAlumno(AlumnoDTO alumnoDTO) {
		AlumnoDTO response = webApiCallerService.post(alumnosServiceUrl + "/alumnos", alumnoDTO, AlumnoDTO.class);
		if (response == null) {
			throw new RuntimeException("Error al crear alumno en el servicio externo");
		}
		return response;
	}

	public AlumnoDTO actualizarAlumno(String legajo, AlumnoDTO alumnoDTO) {
		AlumnoDTO response = webApiCallerService.put(alumnosServiceUrl + "/alumnos/" + legajo, alumnoDTO, AlumnoDTO.class);
		if (response == null) {
			throw new RuntimeException("Error al actualizar alumno en el servicio externo");
		}
		return response;
	}

	public void eliminarAlumno(String legajo) {
		webApiCallerService.delete(alumnosServiceUrl + "/alumnos/" + legajo);
	}

	public boolean existeAlumno(String legajo) {
		try {
			obtenerAlumnoPorLegajo(legajo);
			return true;
		} catch (NotFoundException e) {
			return false;
		} catch (Exception e) {
			throw new RuntimeException("Error al verificar existencia del alumno: " + e.getMessage(), e);
		}
	}*/
}