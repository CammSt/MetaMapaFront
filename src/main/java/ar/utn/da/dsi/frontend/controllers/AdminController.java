package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.client.dto.HechoDTO;
import ar.utn.da.dsi.frontend.client.dto.input.ColeccionInputDTO;
import ar.utn.da.dsi.frontend.client.dto.input.HechoInputDTO;
import ar.utn.da.dsi.frontend.client.dto.output.*;
import ar.utn.da.dsi.frontend.services.colecciones.ColeccionService;
import ar.utn.da.dsi.frontend.services.estadisticas.EstadisticasService;
import ar.utn.da.dsi.frontend.services.hechos.HechoService;
import ar.utn.da.dsi.frontend.services.solicitudes.SolicitudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ar.utn.da.dsi.frontend.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

  private final ColeccionService coleccionService;
  private final SolicitudService solicitudService;
  private final HechoService hechoService;
  private final EstadisticasService estadisticasService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public AdminController(ColeccionService coleccionService, SolicitudService solicitudService, HechoService hechoService, EstadisticasService estadisticasService) {
    this.coleccionService = coleccionService;
    this.solicitudService = solicitudService;
    this.hechoService = hechoService;
    this.estadisticasService = estadisticasService;
  }

  @GetMapping
  public String mostrarPanelAdmin(Model model, Authentication authentication) {
    // 1. Colecciones
    try {
      model.addAttribute("colecciones", coleccionService.obtenerTodas());
    } catch (Exception e) {
      model.addAttribute("colecciones", List.of());
    }

    // 2. UNIFICAR SOLICITUDES (USANDO HISTORIAL COMPLETO)
    List<SolicitudUnificadaDTO> listaUnificada = new ArrayList<>();
    String adminId = (authentication != null) ? authentication.getName() : null;

    if (adminId != null) {
      // A) TODOS los Hechos (Nuevos, Aprobados, Rechazados)
      try {
        // CAMBIO CLAVE: Usamos buscarHistorialHechos() en lugar de buscarHechosPendientes()
        List<HechoDTO> todosHechos = hechoService.buscarHistorialHechos();
        if (todosHechos != null) {
          for (HechoDTO h : todosHechos) {
            if (h.getId() != null) {
              // El estado ya viene en el DTO (PENDIENTE, ACEPTADO, RECHAZADO)
              listaUnificada.add(new SolicitudUnificadaDTO(h.getId(), h.getTitulo(), "Nuevo Hecho", h.getEstado()));
            }
          }
        }
      } catch (Exception e) {
        System.out.println("Error historial hechos: " + e.getMessage());
      }

      // B) Solicitudes de Baja (Ya trae todas)
      try {
        List<SolicitudEliminacionOutputDTO> bajas = solicitudService.obtenerTodasParaAdmin();
        if (bajas != null) {
          for (SolicitudEliminacionOutputDTO b : bajas) {
            if (b.nroDeSolicitud() != null) {
              listaUnificada.add(new SolicitudUnificadaDTO(b.nroDeSolicitud(), b.tituloDelHechoAEliminar(), "Eliminación", b.estado()));
            }
          }
        }
      } catch (Exception e) {
        System.out.println("Error historial bajas: " + e.getMessage());
      }

      // C) TODAS las Ediciones
      try {
        // CAMBIO CLAVE: Usamos buscarHistorialEdiciones() en lugar de buscarEdicionesPendientes()
        List<EdicionOutputDTO> ediciones = hechoService.buscarHistorialEdiciones();
        if (ediciones != null) {
          for (EdicionOutputDTO e : ediciones) {
            if (e.getId() != null) {
              String titulo = (e.getTituloPropuesto() != null) ? e.getTituloPropuesto() : "Edición Hecho ID " + e.getIdHechoOriginal();
              listaUnificada.add(new SolicitudUnificadaDTO(e.getId(), titulo, "Edición", e.getEstado()));
            }
          }
        }
      } catch (Exception e) {
        System.out.println("Error historial ediciones: " + e.getMessage());
      }
    }

    model.addAttribute("solicitudes", listaUnificada);

    // Datos auxiliares
    try {
      model.addAttribute("consensusLabels", hechoService.getConsensusLabels());
      model.addAttribute("availableSources", hechoService.getAvailableSources());
    } catch (Exception e) {
      model.addAttribute("consensusLabels", null);
      model.addAttribute("availableSources", null);
    }

    model.addAttribute("titulo", "Panel de Administración");
    return "admin";
  }

  // --- ENDPOINT MODAL (UNIFICADO) ---
  @GetMapping("/api/solicitudes/{id}/detalle")
  @ResponseBody
  public ResponseEntity<?> getDetalleUnificado(@PathVariable Long id, @RequestParam String tipo) {
    try {
      if ("Nuevo Hecho".equals(tipo)) {
        return ResponseEntity.ok(hechoService.getHechoInputDTOporId(id));
      }
      else if ("Eliminación".equals(tipo)) {
        return ResponseEntity.ok(solicitudService.obtenerPorId(id.intValue(), "admin"));
      }
      else if ("Edición".equals(tipo)) {
        EdicionOutputDTO edicion = hechoService.buscarEdicionPorId(id);
        if (edicion == null) return ResponseEntity.notFound().build();

        HechoInputDTO original = new HechoInputDTO();
        try {
          if (edicion.getIdHechoOriginal() != null) {
            original = hechoService.getHechoInputDTOporId(edicion.getIdHechoOriginal());
          }
        } catch (Exception e) {
          original.setTitulo("No disponible");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("propuesta", edicion);
        response.put("original", original);
        return ResponseEntity.ok(response);
      }
      return ResponseEntity.badRequest().body(Map.of("error", "Tipo desconocido"));
    } catch (Exception e) {
      return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
    }
  }

  // --- ACTIONS Y COLECCIONES (MANTENIDOS) ---
  @PostMapping("/solicitudes/{id}/aprobar")
  public String aprobarSolicitud(@PathVariable("id") Integer id, Authentication auth) {
    solicitudService.aceptar(id, auth.getName());
    return "redirect:/admin?success=solicitud_aprobada";
  }
  @PostMapping("/solicitudes/{id}/rechazar")
  public String rechazarSolicitud(@PathVariable("id") Integer id, Authentication auth) {
    solicitudService.rechazar(id, auth.getName());
    return "redirect:/admin?success=solicitud_rechazada";
  }
  @PostMapping("/hechos/{id}/aprobar")
  public String aprobarHecho(@PathVariable("id") Long id) {
    hechoService.aprobar(id);
    return "redirect:/admin?success=hecho_aprobado";
  }
  @PostMapping("/hechos/{id}/rechazar")
  public String rechazarHecho(@PathVariable("id") Long id) {
    hechoService.rechazar(id);
    return "redirect:/admin?success=hecho_rechazado";
  }
  @PostMapping("/ediciones/{id}/aceptar")
  public String aceptarEdicion(@PathVariable("id") Long id) {
    hechoService.aceptarEdicion(id);
    return "redirect:/admin?success=edicion_aceptada";
  }
  @PostMapping("/ediciones/{id}/rechazar")
  public String rechazarEdicion(@PathVariable("id") Long id) {
    hechoService.rechazarEdicion(id);
    return "redirect:/admin?success=edicion_rechazada";
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
  public String crearColeccion(@ModelAttribute ColeccionInputDTO dto, HttpSession session) {
    dto.setVisualizadorID(getUserIdFromSession(session));
    System.out.println("Creando colección con DTO: " + dto);
    coleccionService.crear(dto);
    return "redirect:/admin?success=coleccion_creada";
  }

  @GetMapping("/colecciones/{id}/editar-completo")
  public String mostrarFormularioEditarColeccionCompleta(@PathVariable("id") String id, Model model) {
    try {
      ColeccionOutputDTO out = coleccionService.obtenerPorId(id);

      ColeccionInputDTO in = new ColeccionInputDTO();
      in.setTitulo(out.getTitulo());
      in.setDescripcion(out.getDescripcion());
      in.setHandleID(out.getHandleID());
      in.setAlgoritmoConsenso(out.getAlgoritmoConsenso());
      in.setFuentes(out.getFuentes());

      // Se usa el modo "irrestricta" por defecto para obtener todos los hechos
      List<HechoDTO> hechosDeColeccion = hechoService.getHechosDeColeccion(id, "irrestricta", null, null, null, null);


      model.addAttribute("coleccionDTO", in);
      model.addAttribute("titulo", "Editar Colección: " + out.getTitulo());
      model.addAttribute("accion", "editar");

      model.addAttribute("consensusLabels", hechoService.getConsensusLabels());
      model.addAttribute("availableSources", hechoService.getAvailableSources());
      model.addAttribute("hechosEnColeccion", hechosDeColeccion);
      model.addAttribute("criteriosActuales", out.getCriterios());
      model.addAttribute("handleId", id);

      return "admin-coleccion-edit-full";
    } catch (Exception e) {
      System.err.println("Error al cargar formulario de edición de colección: " + e.getMessage());
      return "redirect:/admin?error=Error al cargar la colección para edición.";
    }
  }

  @PostMapping("/colecciones/{id}/guardar")
  public String guardarColeccionCompleta(@PathVariable("id") String id,
                                         @ModelAttribute ColeccionInputDTO dto,
                                         Authentication auth,
                                         RedirectAttributes redirectAttributes) {
    try {
      dto.setVisualizadorID(auth.getName());

      coleccionService.actualizar(id, dto);

      redirectAttributes.addFlashAttribute("success", "Colección " + dto.getTitulo() + " actualizada correctamente.");
      return "redirect:/admin?tab=collections";
    } catch (ValidationException e) {
      redirectAttributes.addFlashAttribute("error", "Error de validación: " + e.getFieldErrors().values().iterator().next());
      redirectAttributes.addFlashAttribute("coleccionDTO", dto);
      return "redirect:/admin/colecciones/" + id + "/editar-completo";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error al actualizar la colección: " + e.getMessage());
      redirectAttributes.addFlashAttribute("coleccionDTO", dto);
      return "redirect:/admin/colecciones/" + id + "/editar-completo";
    }
  }

  @PostMapping("/colecciones/{id}/eliminar")
  public String eliminarColeccion(@PathVariable("id") String id, Authentication auth) {
    coleccionService.eliminar(id, auth.getName());
    return "redirect:/admin?success=coleccion_eliminada";
  }
  @PostMapping("/hechos/importar-csv")
  public String importarCsv(@RequestParam("csvFile") MultipartFile file, RedirectAttributes attr) {
    try {
      hechoService.importarCsv(file);
      attr.addFlashAttribute("success", "CSV subido con éxito al sistema.");
    } catch (RuntimeException e) {
      String errorMessage = getWebClientErrorMessage(e);
      attr.addFlashAttribute("error", "Error al importar: " + errorMessage);
    }
    return "redirect:/admin";
  }

  private String getWebClientErrorMessage(RuntimeException e) {
    Throwable cause = e.getCause();
    if (cause instanceof WebClientResponseException) {
      WebClientResponseException webClientEx = (WebClientResponseException) cause;
      String errorBody = webClientEx.getResponseBodyAsString();
      try {
        return objectMapper.readTree(errorBody).path("message").asText(webClientEx.getStatusText());
      } catch (Exception jsonEx) {
        return "Error de comunicación: " + webClientEx.getStatusText() + " (" + webClientEx.getStatusCode().value() + "). Verificá los logs del backend.";
      }
    }
    return e.getMessage();
  }

  // MÉTODO AUXILIAR PARA NO REPETIR CÓDIGO
  private String getUserIdFromSession(HttpSession session) {
    try {
      String userJson = (String) session.getAttribute("userJson");
      if (userJson != null) {
        return String.valueOf(objectMapper.readTree(userJson).get("id").asLong());
      }
    } catch (Exception e) {
      System.err.println("Error obteniendo ID de sesión: " + e.getMessage());
    }
    return null; // O lanzar excepción si preferís
  }

  @GetMapping("/estadisticas")
  public String mostrarEstadisticas(@RequestParam(required = false) String handleIdColeccion, @RequestParam(required = false) String categoriaProvincia, @RequestParam(required = false) String categoriaHora, Model model) {
    try { model.addAttribute("colecciones", coleccionService.obtenerTodas()); } catch (Exception e) { model.addAttribute("colecciones", List.of()); }
    try { model.addAttribute("categorias", hechoService.getAvailableCategories()); } catch (Exception e) { model.addAttribute("categorias", List.of()); }
    try { model.addAttribute("distribucionCategorias", estadisticasService.getDistribucionCategorias()); model.addAttribute("spamRatio", estadisticasService.getSolicitudesSpamRatio()); } catch (Exception e) {}

    if (handleIdColeccion != null && !handleIdColeccion.isEmpty()) {
      try { model.addAttribute("resultadoProvinciaColeccion", estadisticasService.getDistribucionProvinciasPorColeccion(handleIdColeccion)); model.addAttribute("handleIdColeccionSeleccionada", handleIdColeccion); } catch (Exception e) {}
    }
    if (categoriaProvincia != null && !categoriaProvincia.isEmpty()) {
      try { model.addAttribute("resultadoProvinciaCategoria", estadisticasService.getDistribucionProvinciasPorCategoria(categoriaProvincia)); model.addAttribute("categoriaProvinciaSeleccionada", categoriaProvincia); } catch (Exception e) {}
    }
    if (categoriaHora != null && !categoriaHora.isEmpty()) {
      try { model.addAttribute("resultadoHoraCategoria", estadisticasService.getDistribucionHorasPorCategoria(categoriaHora)); model.addAttribute("categoriaHoraSeleccionada", categoriaHora); } catch (Exception e) {}
    }
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