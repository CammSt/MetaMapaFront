package ar.utn.da.dsi.frontend.services.hechos;

import ar.utn.da.dsi.frontend.client.dto.HechoDTO;
import ar.utn.da.dsi.frontend.client.dto.input.HechoInputDTO;
import ar.utn.da.dsi.frontend.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@Service
public class HechoService {

  private final HechoApiService apiClient;

  @Autowired
  public HechoService(HechoApiService apiClient) {
    this.apiClient = apiClient;
  }

  public List<HechoDTO> getHechosDeColeccion(String handleId, String modo, String fechaDesde, String fechaHasta, String categoria, String titulo) {
    return apiClient.getHechosDeColeccion(handleId, modo, fechaDesde, fechaHasta, categoria, titulo);
  }

  public List<HechoDTO> buscarHechosPorUsuario(String userId) {
    return apiClient.getHechosPorUsuario(userId);
  }

  public List<String> getAvailableCategories() {
    return apiClient.getAvailableCategories();
  }

  public Map<String, String> getConsensusLabels() {
    return apiClient.getConsensusLabels();
  }

  public List<String> getAvailableSources() {
    return apiClient.getAvailableSources();
  }

  public HechoDTO crear(HechoInputDTO dto) {
    return apiClient.crearHecho(dto);
  }

  public HechoDTO actualizar(Long id, HechoInputDTO dto) {
    return apiClient.actualizarHecho(id, dto);
  }

  public HechoInputDTO getHechoInputDTOporId(Long id) {
    HechoDTO dtoApi = apiClient.getHechoPorId(id);

    HechoInputDTO dtoForm = new HechoInputDTO();
    dtoForm.setId(dtoApi.id());
    dtoForm.setTitulo(dtoApi.titulo());
    dtoForm.setDescripcion(dtoApi.descripcion());
    dtoForm.setCategoria(dtoApi.categoria());
    dtoForm.setFechaAcontecimiento(dtoApi.fechaAcontecimiento());
    dtoForm.setLatitud(dtoApi.latitud());
    dtoForm.setLongitud(dtoApi.longitud());
    dtoForm.setCollectionHandle(dtoApi.collectionHandle());

    return dtoForm;
  }

  public void importarCsv(MultipartFile file) {
    if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
      throw new ValidationException("Por favor, selecciona un archivo .csv válido.");
    }
    apiClient.importarCsv(file);
  }
}