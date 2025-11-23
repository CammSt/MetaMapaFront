package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.client.dto.HechoDTO;
import ar.utn.da.dsi.frontend.client.dto.output.SolicitudEliminacionOutputDTO;
import ar.utn.da.dsi.frontend.client.dto.output.SolicitudUnificadaDTO;
import ar.utn.da.dsi.frontend.services.hechos.HechoService;
import ar.utn.da.dsi.frontend.services.solicitudes.SolicitudService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/contributor")
public class ContributorController {

  private final SolicitudService solicitudService;
  private final HechoService hechoService;

  public ContributorController(SolicitudService solicitudService, HechoService hechoService) {
    this.solicitudService = solicitudService;
    this.hechoService = hechoService;
  }

  /*@GetMapping
  public String mostrarPanelContribuyente(Model model, Authentication authentication) {
    String userId = authentication.getName();

    try {
      model.addAttribute("misHechos", hechoService.buscarHechosPorUsuario(userId));
    } catch (Exception e) {
      model.addAttribute("misHechos", List.of());
    }

    try {
      model.addAttribute("misSolicitudes", solicitudService.obtenerTodas(userId));
    } catch (Exception e) {
      model.addAttribute("misSolicitudes", List.of());
    }

    return "contributor";
  }*/

  @GetMapping
  public String mostrarPanelContribuyente(Model model, Authentication authentication) {
    String userId = authentication.getName();

    // 1. Traer TODOS los Hechos (Pendientes, Aceptados, Rechazados)
    List<HechoDTO> todosMisHechos = new ArrayList<>();
    try {
      todosMisHechos = hechoService.buscarHechosPorUsuario(userId);
    } catch (Exception e) {
      System.out.println("Error buscando hechos: " + e.getMessage());
    }

    // --- FILTRO NUEVO PARA LA TABLA 1 ---
    // Solo mostramos en "Mis Hechos Publicados" los que están ACEPTADO
    List<HechoDTO> hechosPublicados = todosMisHechos.stream()
        .filter(h -> "ACEPTADO".equals(h.estado()))
        .collect(Collectors.toList());

    model.addAttribute("misHechos", hechosPublicados);
    // ------------------------------------

    // 2. Traer Solicitudes de Baja (Eliminaciones)
    List<SolicitudEliminacionOutputDTO> misBajas = new ArrayList<>();
    try {
      misBajas = solicitudService.obtenerTodas(userId);
    } catch (Exception e) {
      System.out.println("Error buscando solicitudes: " + e.getMessage());
    }

    // 3. UNIFICAR TODO para la pestaña "Estado de Solicitudes" (Tabla 2)
    // Acá usamos 'todosMisHechos' porque queremos ver el estado de los pendientes también
    List<SolicitudUnificadaDTO> listaUnificada = new ArrayList<>();

    // A) Convertir Hechos a SolicitudUnificada
    for (HechoDTO h : todosMisHechos) {
      String estadoHecho = (h.estado() != null) ? h.estado() : "EN_REVISION";

      listaUnificada.add(new SolicitudUnificadaDTO(
          h.titulo(),
          "Nuevo Hecho",
          estadoHecho
      ));
    }

    // B) Convertir Bajas a SolicitudUnificada
    for (SolicitudEliminacionOutputDTO baja : misBajas) {
      listaUnificada.add(new SolicitudUnificadaDTO(
          baja.tituloDelHechoAEliminar(),
          "Eliminación",
          baja.estado()
      ));
    }

    model.addAttribute("misSolicitudes", listaUnificada);

    return "contributor";
  }
}