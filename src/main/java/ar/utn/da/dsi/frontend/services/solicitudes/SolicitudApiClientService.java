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

	public List<SolicitudEliminacionOutputDTO> obtenerTodas(String visualizadorId) {
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
		String url = solicitudesApiUrl + "/" + id;

		// Usamos ParameterizedTypeReference para capturar ApiResponse<DTO>
		return apiClientService.executeWithToken(accessToken ->
				apiClientService.getWebClient().get()
						.uri(url)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
						.retrieve()
						.bodyToMono(new ParameterizedTypeReference<ApiResponse<SolicitudEliminacionOutputDTO>>() {})
						.map(response -> response.getDatos()) // Extraemos el objeto real de 'datos'
						.block()
		);
	}

	public SolicitudEliminacionOutputDTO crear(SolicitudEliminacionInputDTO dto) {
		// También aplicamos la corrección aquí por seguridad, ya que el controller devuelve ApiResponse
		SolicitudEliminacionOutputDTO s = apiClientService.executeWithToken(accessToken ->
				apiClientService.getWebClient().post()
						.uri(solicitudesApiUrl)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
						.bodyValue(dto)
						.retrieve()
						.bodyToMono(new ParameterizedTypeReference<ApiResponse<SolicitudEliminacionOutputDTO>>() {})
						.map(response -> response.getDatos())
						.block()
		);
		System.out.println("s = " + s);
		return s;
	}

	public SolicitudEliminacionOutputDTO aceptar(Integer id, String visualizadorId) {
		String url = solicitudesApiUrl + "/" + id + "?aceptado=true";
		// Corrección del desempaquetado
		return apiClientService.executeWithToken(accessToken ->
				apiClientService.getWebClient().put()
						.uri(url)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
						.retrieve()
						.bodyToMono(new ParameterizedTypeReference<ApiResponse<SolicitudEliminacionOutputDTO>>() {})
						.map(response -> response.getDatos())
						.block()
		);
	}

	public SolicitudEliminacionOutputDTO rechazar(Integer id, String visualizadorId) {
		String url = solicitudesApiUrl + "/" + id + "?aceptado=false";
		// Corrección del desempaquetado
		return apiClientService.executeWithToken(accessToken ->
				apiClientService.getWebClient().put()
						.uri(url)
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
						.retrieve()
						.bodyToMono(new ParameterizedTypeReference<ApiResponse<SolicitudEliminacionOutputDTO>>() {})
						.map(response -> response.getDatos())
						.block()
		);
	}
}