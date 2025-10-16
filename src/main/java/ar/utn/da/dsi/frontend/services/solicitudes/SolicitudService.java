package ar.utn.da.dsi.frontend.services.solicitudes;

import ar.utn.da.dsi.frontend.client.dto.input.SolicitudEliminacionInputDTO;
import ar.utn.da.dsi.frontend.client.dto.output.SolicitudEliminacionOutputDTO;
import ar.utn.da.dsi.frontend.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SolicitudService {

	private final SolicitudApiClientService apiClient;

	@Autowired
	public SolicitudService(SolicitudApiClientService apiClient) {
		this.apiClient = apiClient;
	}

	public List<SolicitudEliminacionOutputDTO> obtenerTodas(String visualizadorId) {
		// Aquí podrías agregar lógica de cacheo o filtrado en el futuro
		return apiClient.obtenerTodas(visualizadorId);
	}

	public SolicitudEliminacionOutputDTO obtenerPorId(Integer id, String visualizadorId) {
		return apiClient.obtenerPorId(id, visualizadorId);
	}

	public SolicitudEliminacionOutputDTO crear(SolicitudEliminacionInputDTO dto) {
		validarSolicitud(dto);

		return apiClient.crear(dto);
	}

	public SolicitudEliminacionOutputDTO aceptar(Integer id, String visualizadorId) {
		return apiClient.aceptar(id, visualizadorId);
	}

	public SolicitudEliminacionOutputDTO rechazar(Integer id, String visualizadorId) {
		return apiClient.rechazar(id, visualizadorId);
	}

	private void validarSolicitud(SolicitudEliminacionInputDTO dto) {
		ValidationException validationException = new ValidationException("Errores de validación");
		boolean tieneErrores = false;

		if (dto.getTituloHecho() == null || dto.getTituloHecho().trim().isEmpty()) {
			validationException.addFieldError("tituloHecho", "El título del hecho no puede estar vacío.");
			tieneErrores = true;
		}

		if (dto.getMotivo() == null || dto.getMotivo().trim().length() < 500) {
			validationException.addFieldError("motivo", "El motivo debe tener al menos 500 caracteres.");
			tieneErrores = true;
		}

		if (tieneErrores) {
			throw validationException;
		}
	}
}