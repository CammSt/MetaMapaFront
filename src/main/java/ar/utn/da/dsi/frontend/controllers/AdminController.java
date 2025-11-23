package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.client.dto.input.ColeccionInputDTO;
import ar.utn.da.dsi.frontend.client.dto.output.ColeccionOutputDTO;
import ar.utn.da.dsi.frontend.client.dto.output.HoraHechosPorCategoriaListDTO;
import ar.utn.da.dsi.frontend.client.dto.output.ProvinciaHechosPorCategoriaListDTO;
import ar.utn.da.dsi.frontend.client.dto.output.ProvinciaHechosPorColeccionListDTO;
import ar.utn.da.dsi.frontend.services.colecciones.ColeccionService;
import ar.utn.da.dsi.frontend.services.estadisticas.EstadisticasService;
import ar.utn.da.dsi.frontend.services.hechos.HechoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ar.utn.da.dsi.frontend.services.solicitudes.SolicitudService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private final ColeccionService coleccionService;
  private final SolicitudService solicitudService;
  private final HechoService hechoService;
  private final EstadisticasService estadisticasService;

  public AdminController(ColeccionService coleccionService, SolicitudService solicitudService, HechoService hechoService, EstadisticasService estadisticasService) {
    this.coleccionService = coleccionService;
    this.solicitudService = solicitudService;
    this.hechoService = hechoService;
    this.estadisticasService = estadisticasService;
  }

  @GetMapping
  public String mostrarPanelAdmin(Model model, Authentication authentication) {

    try {
      model.addAttribute("colecciones", coleccionService.obtenerTodas());
    } catch (Exception e) {
      model.addAttribute("colecciones", List.of());
    }

    String adminId = (authentication != null) ? authentication.getName() : null;

    try {
      if (adminId != null) {
        model.addAttribute("solicitudes", solicitudService.obtenerTodas(adminId));
      } else {
        model.addAttribute("solicitudes", List.of());
      }

      model.addAttribute("consensusLabels", hechoService.getConsensusLabels());
      model.addAttribute("availableSources", hechoService.getAvailableSources());

    } catch (Exception e) {
      model.addAttribute("solicitudes", List.of());
      model.addAttribute("consensusLabels", Map.of());
      model.addAttribute("availableSources", List.of());
    }

    model.addAttribute("titulo", "Panel de Administración");
    return "admin";
  }

  @PostMapping("/solicitudes/{id}/aprobar")
  public String aprobarSolicitud(@PathVariable("id") Integer id, Authentication authentication) {
    String adminId = authentication.getName();

    solicitudService.aceptar(id, adminId);

    return "redirect:/admin?success=aprobada";
  }

  @PostMapping("/solicitudes/{id}/rechazar")
  public String rechazarSolicitud(@PathVariable("id") Integer id, Authentication authentication) {
    String adminId = authentication.getName();

    solicitudService.rechazar(id, adminId);

    return "redirect:/admin?success=rechazada";
  }

  @GetMapping("/colecciones/nueva")
  public String mostrarFormularioNuevaColeccion(Model model) {
    model.addAttribute("coleccionDTO", new ColeccionInputDTO());
    model.addAttribute("titulo", "Crear Nueva Colección");
    model.addAttribute("accion", "crear");

    model.addAttribute("consensusLabels", hechoService.getConsensusLabels());

    return "admin-coleccion-form";
  }

  @PostMapping("/colecciones/crear")
  public String crearColeccion(@ModelAttribute ColeccionInputDTO coleccionDTO, Authentication auth) {
    String adminId = auth.getName();
    coleccionDTO.setVisualizadorID(adminId);

    coleccionService.crear(coleccionDTO);

    return "redirect:/admin?success=coleccion_creada";
  }

  @GetMapping("/colecciones/{id}/editar")
  public String mostrarFormularioEditarColeccion(@PathVariable("id") String id, Model model) {
    ColeccionOutputDTO dtoOutput = coleccionService.obtenerPorId(id);
    ColeccionInputDTO dtoInput = new ColeccionInputDTO();
    dtoInput.setTitulo(dtoOutput.titulo());
    dtoInput.setDescripcion(dtoOutput.descripcion());
    // dtoInput.setAlgoritmoConsenso(dtoOutput.algoritmoConsenso()); // OutputDTO no lo tiene
    dtoInput.setHandleID(dtoOutput.handleID());

    model.addAttribute("coleccionDTO", dtoInput);
    model.addAttribute("titulo", "Editar Colección: " + dtoOutput.titulo());
    model.addAttribute("accion", "editar");
    model.addAttribute("consensusLabels", hechoService.getConsensusLabels());

    return "admin-coleccion-form";
  }

  @PostMapping("/colecciones/{id}/editar")
  public String editarColeccion(@PathVariable("id") String id,
                                @ModelAttribute ColeccionInputDTO coleccionDTO,
                                Authentication auth) {

    String adminId = auth.getName();
    coleccionDTO.setVisualizadorID(adminId);

    coleccionService.actualizar(id, coleccionDTO);

    return "redirect:/admin?success=coleccion_editada";
  }

  @PostMapping("/colecciones/{id}/eliminar")
  public String eliminarColeccion(@PathVariable("id") String id, Authentication auth) {
    String adminId = auth.getName();

    coleccionService.eliminar(id, adminId);

    return "redirect:/admin?success=coleccion_eliminada";
  }

  @PostMapping("/hechos/importar-csv")
  public String importarHechosCsv(@RequestParam("csvFile") MultipartFile file, RedirectAttributes redirectAttributes) {

    try {
      hechoService.importarCsv(file);
      redirectAttributes.addFlashAttribute("success", "El archivo CSV se está procesando.");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al subir: " + e.getMessage());
    }

    return "redirect:/admin";
  }

  @GetMapping("/estadisticas")
  public String mostrarEstadisticas(
      @RequestParam(required = false) String handleIdColeccion,
      @RequestParam(required = false) String categoriaProvincia,
      @RequestParam(required = false) String categoriaHora,
      Model model) {

    // 1. Cargar Listas para Dropdowns
    try {
      model.addAttribute("colecciones", coleccionService.obtenerTodas());
    } catch (Exception e) {
      model.addAttribute("colecciones", List.of());
    }
    try {
      model.addAttribute("categorias", hechoService.getAvailableCategories());
    } catch (Exception e) {
      model.addAttribute("categorias", List.of());
    }

    // 2. Cargar Métricas Fijas (2 y 5)
    try {
      model.addAttribute("distribucionCategorias", estadisticasService.getDistribucionCategorias());
      model.addAttribute("spamRatio", estadisticasService.getSolicitudesSpamRatio());
    } catch (Exception e) {
      // Dejar como null si falla, la vista maneja el error
    }

    // 3. Cargar Métricas Parametrizadas (1, 3, 4) si los parámetros están presentes

    // Métrica 1: Distribución de provincias por colección
    if (handleIdColeccion != null && !handleIdColeccion.isEmpty()) {
      try {
        ProvinciaHechosPorColeccionListDTO resultado = estadisticasService.getDistribucionProvinciasPorColeccion(handleIdColeccion);
        model.addAttribute("resultadoProvinciaColeccion", resultado);
        model.addAttribute("handleIdColeccionSeleccionada", handleIdColeccion);
      } catch (Exception e) {
        model.addAttribute("errorProvinciaColeccion", "Error al cargar la distribución de hechos por colección. Asegúrese de que existen datos calculados.");
      }
    }

    // Métrica 3: Distribución de provincias por categoría
    if (categoriaProvincia != null && !categoriaProvincia.isEmpty()) {
      try {
        ProvinciaHechosPorCategoriaListDTO resultado = estadisticasService.getDistribucionProvinciasPorCategoria(categoriaProvincia);
        model.addAttribute("resultadoProvinciaCategoria", resultado);
        model.addAttribute("categoriaProvinciaSeleccionada", categoriaProvincia);
      } catch (Exception e) {
        model.addAttribute("errorProvinciaCategoria", "Error al cargar la distribución de provincia por categoría. Asegúrese de que existen datos calculados.");
      }
    }

    // Métrica 4: Distribución de horas por categoría
    if (categoriaHora != null && !categoriaHora.isEmpty()) {
      try {
        HoraHechosPorCategoriaListDTO resultado = estadisticasService.getDistribucionHorasPorCategoria(categoriaHora);
        model.addAttribute("resultadoHoraCategoria", resultado);
        model.addAttribute("categoriaHoraSeleccionada", categoriaHora);
      } catch (Exception e) {
        model.addAttribute("errorHoraCategoria", "Error al cargar la distribución de hora por categoría. Asegúrese de que existen datos calculados.");
      }
    }

    // 4. URLs de Exportación
    model.addAttribute("urlZipCompleto", estadisticasService.getExportUrlZipCompleto());
    model.addAttribute("urlExportarProvinciaColeccion", estadisticasService.getExportUrlProvinciaColeccion());
    model.addAttribute("urlExportarCategoriaHechos", estadisticasService.getExportUrlCategoriaHechos());
    model.addAttribute("urlExportarProvinciaCategoria", estadisticasService.getExportUrlProvinciaCategoria());
    model.addAttribute("urlExportarHoraCategoria", estadisticasService.getExportUrlHoraCategoria());
    model.addAttribute("urlExportarSpam", estadisticasService.getExportUrlSpam());

    model.addAttribute("titulo", "Estadísticas");
    return "estadisticas";
  }
}