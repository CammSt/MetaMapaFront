package ar.utn.da.dsi.frontend.services.solicitudes;

import ar.utn.da.dsi.frontend.client.dto.input.SolicitudEliminacionInputDTO;
import ar.utn.da.dsi.frontend.client.dto.output.SolicitudEliminacionOutputDTO;
import ar.utn.da.dsi.frontend.services.ApiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SolicitudApiClientService {

	private final ApiClientService apiClientService;
	private final String solicitudesApiUrl;

	@Autowired
	public SolicitudApiClientService(ApiClientService apiClientService, @Value("${solicitudes.service.url}") String solicitudesApiUrl) {
		this.apiClientService = apiClientService;
		this.solicitudesApiUrl = solicitudesApiUrl;
	}

	public List<SolicitudEliminacionOutputDTO> obtenerTodas(String visualizadorId) {
		String url = solicitudesApiUrl + "/mostrar-solicitudes?id=" + visualizadorId;
		return apiClientService.getList(url, SolicitudEliminacionOutputDTO.class);
	}

	public SolicitudEliminacionOutputDTO obtenerPorId(Integer id, String visualizadorId) {
		String url = solicitudesApiUrl + "/mostrar-solicitud/" + id + "?idVisualizador=" + visualizadorId;
		return apiClientService.get(url, SolicitudEliminacionOutputDTO.class);
	}

	public SolicitudEliminacionOutputDTO crear(SolicitudEliminacionInputDTO dto) {
		return apiClientService.post(solicitudesApiUrl, dto, SolicitudEliminacionOutputDTO.class);
	}

	public SolicitudEliminacionOutputDTO aceptar(Integer id, String visualizadorId) {
		String url = solicitudesApiUrl + "/" + id + "?visualizadorId=" + visualizadorId + "&aceptado=true";
		return apiClientService.put(url, null, SolicitudEliminacionOutputDTO.class);
	}

	public SolicitudEliminacionOutputDTO rechazar(Integer id, String visualizadorId) {
		String url = solicitudesApiUrl + "/" + id + "?visualizadorId=" + visualizadorId + "&aceptado=false";
		return apiClientService.put(url, null, SolicitudEliminacionOutputDTO.class);
	}
}
