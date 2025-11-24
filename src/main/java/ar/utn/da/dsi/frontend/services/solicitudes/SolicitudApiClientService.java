package ar.utn.da.dsi.frontend.services.solicitudes;

import ar.utn.da.dsi.frontend.client.dto.ApiResponse;
import ar.utn.da.dsi.frontend.client.dto.input.SolicitudEliminacionInputDTO;
import ar.utn.da.dsi.frontend.client.dto.output.SolicitudEliminacionOutputDTO;
import ar.utn.da.dsi.frontend.services.ApiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SolicitudApiClientService {

	private final ApiClientService apiClientService;
	private final String solicitudesApiUrl;

	@Autowired
	public SolicitudApiClientService(ApiClientService apiClientService, @Value("${agregador.solicitudes.service.url}") String solicitudesApiUrl) {
		this.apiClientService = apiClientService;
		this.solicitudesApiUrl = solicitudesApiUrl;
	}

	public List<SolicitudEliminacionOutputDTO> obtenerTodasParaAdmin() {
		// Llama a GET http://localhost:8081/solicitudes (Sin params)
		return apiClientService.executeWithToken(accessToken ->
				apiClientService.getWebClient().get()
						.uri(solicitudesApiUrl)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
						.retrieve()
						.bodyToMono(new ParameterizedTypeReference<ApiResponse<List<SolicitudEliminacionOutputDTO>>>() {})
						.map(response -> response.getDatos())
						.block()
		);
	}

	// Método para Contribuyente (Mis Solicitudes)
	public List<SolicitudEliminacionOutputDTO> obtenerTodas(String visualizadorId) {
		// Llama a /mis-solicitudes (usa el token para filtrar en backend, el visualizadorId parámetro ya no es estricto pero lo dejamos por compatibilidad)
		String url = solicitudesApiUrl + "/mis-solicitudes";

		return apiClientService.executeWithToken(accessToken ->
				apiClientService.getWebClient().get()
						.uri(url)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
						.retrieve()
						.bodyToMono(new ParameterizedTypeReference<ApiResponse<List<SolicitudEliminacionOutputDTO>>>() {})
						.map(response -> response.getDatos())
						.block()
		);
	}

	public SolicitudEliminacionOutputDTO obtenerPorId(Integer id, String visualizadorId) {
		// El Backend es GET /solicitudes/{id} y usa el token para la autorización.
		String url = solicitudesApiUrl + "/" + id;
		return apiClientService.get(url, SolicitudEliminacionOutputDTO.class);
	}

	public SolicitudEliminacionOutputDTO crear(SolicitudEliminacionInputDTO dto) {
		// El Backend (POST /solicitudes) requiere autenticación (token) para obtener el userId.
		return apiClientService.post(solicitudesApiUrl, dto, SolicitudEliminacionOutputDTO.class);
	}

	public SolicitudEliminacionOutputDTO aceptar(Integer id, String visualizadorId) {
		//El Backend es PUT /solicitudes/{id}?aceptado=true y usa el token para la identidad.
		String url = solicitudesApiUrl + "/" + id + "?aceptado=true";
		return apiClientService.put(url, null, SolicitudEliminacionOutputDTO.class);
	}

	public SolicitudEliminacionOutputDTO rechazar(Integer id, String visualizadorId) {
		//El Backend es PUT /solicitudes/{id}?aceptado=false y usa el token para la identidad.
		String url = solicitudesApiUrl + "/" + id + "?aceptado=false";
		return apiClientService.put(url, null, SolicitudEliminacionOutputDTO.class);
	}
}
