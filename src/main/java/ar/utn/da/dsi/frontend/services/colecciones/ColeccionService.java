package ar.utn.da.dsi.frontend.services.colecciones;

import ar.utn.da.dsi.frontend.client.dto.input.ColeccionInputDTO;
import ar.utn.da.dsi.frontend.client.dto.output.ColeccionOutputDTO;
import ar.utn.da.dsi.frontend.exceptions.ValidationException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ColeccionService {
	private final ColeccionApiService apiClient;

	public ColeccionService(ColeccionApiService apiClient) {
		this.apiClient = apiClient;
	}

	public List<ColeccionOutputDTO> obtenerTodas() {
		return apiClient.obtenerTodas();
	}

	public ColeccionOutputDTO obtenerPorId(String id) {
		return apiClient.obtenerPorId(id);
	}

	public void crear(ColeccionInputDTO dto) {
		validarDatosColeccion(dto);
		apiClient.crear(dto);
	}

	public void actualizar(String id, ColeccionInputDTO dto) {
		validarDatosColeccion(dto); // Valida al menos título, descripción y algoritmo (si es nuevo)

		apiClient.actualizarDatosBasicos(id, dto);

		apiClient.editarConfiguracionUnificada(id, dto);
	}

	public void eliminar(String id, String visualizadorID) {
		apiClient.eliminar(id, visualizadorID);
	}

	private void validarDatosColeccion(ColeccionInputDTO dto) {
		ValidationException validationException = new ValidationException("Errores de validación");
		boolean tieneErrores = false;

		if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()) {
			validationException.addFieldError("titulo", "El título es obligatorio");
			tieneErrores = true;
		}
		if (dto.getDescripcion() == null || dto.getDescripcion().trim().isEmpty()) {
			validationException.addFieldError("descripcion", "La descripción es obligatoria");
			tieneErrores = true;
		}
		if( dto.getAlgoritmoConsenso() == null || dto.getAlgoritmoConsenso().trim().isEmpty()) {
			validationException.addFieldError("algoritmoConsenso", "El algoritmo de consenso es obligatorio");
			tieneErrores = true;
		}

		if (tieneErrores) {
			throw validationException;
		}
	}
}
