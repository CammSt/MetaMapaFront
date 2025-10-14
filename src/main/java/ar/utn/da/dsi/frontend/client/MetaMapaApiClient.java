package ar.utn.da.dsi.frontend.client;

import ar.utn.da.dsi.frontend.client.dto.HechoDTO;
import ar.utn.da.dsi.frontend.client.dto.SolicitudDTO;
import ar.utn.da.dsi.frontend.services.MockDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class MetaMapaApiClient {

	private final MockDataService mockData;

	@Autowired
	public MetaMapaApiClient(MockDataService mockData) { this.mockData = mockData; }

	public List<ColeccionDTO> getColecciones() { return mockData.getColecciones(); }
	public ColeccionDTO getColeccion(String handleId) { return mockData.getColeccion(handleId); }
	public List<HechoDTO> getHechosDeColeccion(String handleId) { return mockData.getHechosDeColeccion(handleId); }
	public List<SolicitudDTO> getSolicitudes() { return mockData.getSolicitudes(); }
	// Simulación para los paneles, ya que no tenemos un backend de usuarios
	public List<HechoDTO> getHechosPorUsuario(Long userId) { return mockData.getHechosDeColeccion("desapariciones-odio"); }
	public List<SolicitudDTO> getSolicitudesPorUsuario(Long userId) { return mockData.getSolicitudes(); }
	public List<String> getAvailableCategories() { return mockData.getAvailableCategories(); }
	public Map<String, String> getConsensusLabels() { return mockData.getConsensusLabels(); }
	public List<String> getAvailableSources() { return mockData.getAvailableSources(); }
}