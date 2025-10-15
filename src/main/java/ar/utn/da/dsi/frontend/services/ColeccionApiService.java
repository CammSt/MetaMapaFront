package ar.utn.da.dsi.frontend.services;

import ar.utn.da.dsi.frontend.client.dto.input.ColeccionInputDTO;
import ar.utn.da.dsi.frontend.client.dto.output.ColeccionOutputDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ColeccionApiService {
	private final ApiClientService apiClientService;
	private final String coleccionesApiUrl;

	@Autowired
	public ColeccionApiService(ApiClientService apiClientService, @Value("${colecciones.service.url}") String coleccionesApiUrl) {
		this.apiClientService = apiClientService;
		this.coleccionesApiUrl = coleccionesApiUrl;
	}

	public List<ColeccionOutputDTO> obtenerTodas() {
		return apiClientService.getList(coleccionesApiUrl, ColeccionOutputDTO.class);
	}

	public ColeccionOutputDTO obtenerPorId(String id) {
		return apiClientService.get(coleccionesApiUrl + "/" + id, ColeccionOutputDTO.class);
	}

	public void crear(ColeccionInputDTO dto) {
		// El endpoint de tu backend no devuelve la colección creada, así que usamos Void.class
		apiClientService.post(coleccionesApiUrl, dto, Void.class);
	}

	public void actualizar(String id, ColeccionInputDTO dto) {
		apiClientService.put(coleccionesApiUrl + "/" + id, dto, Void.class);
	}

	public void eliminar(String id, String visualizadorID) {
		String urlConParam = coleccionesApiUrl + "/" + id + "?visualizadorID=" + visualizadorID;
		apiClientService.delete(urlConParam);
	}
}