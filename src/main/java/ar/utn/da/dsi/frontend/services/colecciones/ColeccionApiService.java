package ar.utn.da.dsi.frontend.services.colecciones;

import ar.utn.da.dsi.frontend.client.dto.input.ColeccionInputDTO;
import ar.utn.da.dsi.frontend.client.dto.output.ColeccionOutputDTO;
import ar.utn.da.dsi.frontend.services.ApiClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class ColeccionApiService {
	private final ApiClientService apiClientService;
	private final String coleccionesApiUrl;

	@Autowired
	public ColeccionApiService(ApiClientService apiClientService, @Value("${agregador.colecciones.service.url}") String coleccionesApiUrl) {
		this.apiClientService = apiClientService;
		this.coleccionesApiUrl = coleccionesApiUrl;
	}

	public List<ColeccionOutputDTO> obtenerTodas() {
		return apiClientService.getListPublic(coleccionesApiUrl, ColeccionOutputDTO.class);
	}

	public ColeccionOutputDTO obtenerPorId(String id) {
		return apiClientService.getPublic(coleccionesApiUrl + "/" + id, ColeccionOutputDTO.class);
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

	// PUT /colecciones/{id}/consenso (Cambiar Algoritmo)
	public void cambiarAlgoritmoConsenso(String id, String algoritmo, String visualizadorID) {
		// Endpoint PUT /colecciones/{id}/consenso?algoritmo={alg}&visualizadorID={id}
		String url = coleccionesApiUrl + "/" + id + "/consenso";
		String urlConParam = url + "?algoritmo=" + algoritmo + "&visualizadorID=" + visualizadorID;
		apiClientService.put(urlConParam, null, Void.class);
	}

	// PUT /colecciones/{handleId}/fuentes (Añadir/Modificar Fuentes)
	public void modificarFuentes(String handleId, List<String> fuentes, String visualizadorID) {
		// Endpoint PUT /colecciones/{handleId}/fuentes?visualizadorID={id}
		String url = coleccionesApiUrl + "/" + handleId + "/fuentes" + "?visualizadorID=" + visualizadorID;
		apiClientService.put(url, fuentes, Void.class);
	}

	// DELETE /colecciones/{handleId}/fuentes (Quitar Fuentes con Body)
	public void quitarFuentes(String handleId, List<String> fuentes, String visualizadorID) {
		// Endpoint DELETE /colecciones/{handleId}/fuentes?visualizadorID={id}
		String url = coleccionesApiUrl + "/" + handleId + "/fuentes" + "?visualizadorID=" + visualizadorID;
		// Usa el método especial deleteWithBody del ApiClientService
		apiClientService.deleteWithBody(url, fuentes);
	}

	// PUT /colecciones/{handleID}/criterios (Editar Criterios de Pertenencia)
	public void editarCriteriosDePertenencia(String handleID, List<String> criterios, List<String> valoresCriterios, String visualizadorID) {
		// Endpoint PUT /colecciones/{handleID}/criterios?visualizadorID={id}
		String url = coleccionesApiUrl + "/" + handleID + "/criterios" + "?visualizadorID=" + visualizadorID;

		// El Backend espera las dos listas, lo enviamos como un Map encapsulado en el cuerpo.
		Map<String, List<String>> body = Map.of(
				"criterios", criterios,
				"valoresParaCriterios", valoresCriterios
		);

		apiClientService.put(url, body, Void.class);
	}

}