package ar.utn.da.dsi.frontend.services;

import ar.utn.da.dsi.frontend.client.dto.HechoDTO;
import ar.utn.da.dsi.frontend.client.dto.SolicitudDTO;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MockDataService {

	private final List<ColeccionDTO> colecciones;
	private final List<HechoDTO> hechos;
	private final List<SolicitudDTO> solicitudes;
	private final List<String> availableCategories = List.of("Crimen de Odio", "Desaparición Forzada", "Incendio Forestal");

	// Datos hardcodeados del viejo data.js
	private final Map<String, String> consensusLabels = Map.of("none", "No Especificado", "multiple", "Múltiples Menciones", "majority", "Mayoría Simple", "absolute", "Absoluta");
	private final List<String> availableSources = List.of("Carga Manual", "API Sudoeste", "Datos Abiertos GCBA");


	public MockDataService() {
		this.colecciones = new ArrayList<>(List.of(
				new ColeccionDTO("Desapariciones por crímenes de odio", "Recopilación de casos de desapariciones forzadas.", "desapariciones-odio", "Mayoría Simple"),
				new ColeccionDTO("Incendios forestales en Argentina 2025", "Seguimiento de focos de incendio y áreas afectadas.", "incendios-2025", "Múltiples Menciones")
		));

		this.hechos = new ArrayList<>(List.of(
				/* Hechos para "Desapariciones por crímenes de odio" */
				new HechoDTO(101L, 1L, "desapariciones-odio", "Caso J.P. (La Plata)", "BLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLA", "Crimen de Odio", LocalDateTime.of(2024, 5, 10, 0, 0), LocalDate.now(), -34.9213, -57.9545, true),
				new HechoDTO(102L, 2L, "desapariciones-odio", "Incidente en Barrio Norte", "BLABLABLABLABLABLABLABLABLABLABLABLA", "Crimen de Odio", LocalDateTime.of(2024, 8, 22, 0, 0), LocalDate.now(), -34.5888, -58.3977, false),
				new HechoDTO(103L, 1L, "desapariciones-odio", "Ataque en Rosario", "BLABLABLABLABLABLABLABLABLABLABLABLA", "Crimen de Odio", LocalDateTime.of(2025, 2, 20, 0, 0), LocalDate.now(), -32.9478, -60.6305, true),
				/* Hechos para "Incendios forestales en Argentina 2025" */
				new HechoDTO(201L, 1L, "incendios-2025", "Incendio en Sierras de Córdoba", "BLABLABLABLABLABLABLABLABLABLABLABLA", "Incendio Forestal", LocalDateTime.of(2025, 1, 15, 0, 0), LocalDate.now(), -31.4201, -64.1888, true),
				new HechoDTO(202L, 1L, "incendios-2025", "Foco en el Delta del Paraná", "BLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLABLA", "Incendio Forestal", LocalDateTime.of(2025, 4, 2, 0, 0), LocalDate.now(), -34.0333, -58.9167, true),
				new HechoDTO(203L, 2L, "incendios-2025", "Incendio en los Esteros del Iberá", "BLABLABLABLABLABLABLABLABLABLABLABLA", "Incendio Forestal", LocalDateTime.of(2025, 8, 10, 0, 0), LocalDate.now(), -28.5444, -57.7306, false)
		));

		this.solicitudes = new ArrayList<>(List.of(
				new SolicitudDTO(1L, 1L, "Emisiones no declaradas en Dock Sud", "PENDIENTE", "Motivo de prueba 1...", "Nuevo Hecho"),
				new SolicitudDTO(2L, 99L, "Caso J.P. (La Plata)", "PENDIENTE", "Motivo de prueba 2...", "Eliminación"),
				new SolicitudDTO(3L, 1L, "Incendio en Sierras de Córdoba", "APROBADA", "Motivo de prueba 3...", "Eliminación"),
				new SolicitudDTO(4L, 1L, "Tala en Parque Pereyra", "RECHAZADA", "Motivo de prueba 4...", "Nuevo Hecho")
		));
	}


	// --- Getters para los datos ---
	public List<ColeccionDTO> getColecciones() { return this.colecciones; }
	public ColeccionDTO getColeccion(String handleId) { return this.colecciones.stream().filter(c -> c.handleID().equals(handleId)).findFirst().orElse(null); }
	public List<HechoDTO> getHechosDeColeccion(String handleId) { return this.hechos.stream().filter(h -> h.collectionHandle().equals(handleId)).collect(Collectors.toList()); }
	public List<HechoDTO> getHechosPorUsuario(Long userId) { return this.hechos.stream().filter(h -> h.userId().equals(userId)).collect(Collectors.toList()); }
	public List<SolicitudDTO> getSolicitudes() { return this.solicitudes; }
	public List<SolicitudDTO> getSolicitudesPorUsuario(Long userId) { return this.solicitudes.stream().filter(s -> s.userId().equals(userId)).collect(Collectors.toList()); }
	public Map<String, String> getConsensusLabels() { return this.consensusLabels; }
	public List<String> getAvailableSources() { return this.availableSources; }
	public List<String> getAvailableCategories() { return this.availableCategories; }
}