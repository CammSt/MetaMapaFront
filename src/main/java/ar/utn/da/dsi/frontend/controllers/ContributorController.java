package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.client.dto.HechoDTO;
import ar.utn.da.dsi.frontend.client.dto.output.EdicionOutputDTO;
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

  @GetMapping
  public String mostrarPanelContribuyente(Model model, Authentication authentication) {
    String userId = authentication.getName();

    // ==========================================
    // PASO 1: OBTENER DATOS DE LAS APIs
    // ==========================================

    // A) Traer TODOS los Hechos (Pendientes, Aceptados, Rechazados)
    List<HechoDTO> todosMisHechos = new ArrayList<>();
    try {
      todosMisHechos = hechoService.buscarHechosPorUsuario(userId);
    } catch (Exception e) {
      System.out.println("Error buscando hechos: " + e.getMessage());
    }

    // B) Traer Solicitudes de Baja (Eliminaciones)
    List<SolicitudEliminacionOutputDTO> misBajas = new ArrayList<>();
    try {
      misBajas = solicitudService.obtenerTodas(userId);
    } catch (Exception e) {
      System.out.println("Error buscando solicitudes de baja: " + e.getMessage());
    }

    // C) Traer Ediciones (NUEVO)
    List<EdicionOutputDTO> misEdiciones = new ArrayList<>();
    try {
      misEdiciones = hechoService.buscarEdicionesPorUsuario(userId);
    } catch (Exception e) {
      System.out.println("Error buscando ediciones: " + e.getMessage());
    }

    // ==========================================
    // PASO 2: PREPARAR DATOS PARA LA VISTA
    // ==========================================

    // --- TABLA 1: "Mis Hechos Publicados" ---
    // Solo mostramos los que ya están ACEPTADO
    List<HechoDTO> hechosPublicados = todosMisHechos.stream()
        .filter(h -> "ACEPTADO".equals(h.estado()))
        .collect(Collectors.toList());

    model.addAttribute("misHechos", hechosPublicados);


    // --- TABLA 2: "Estado de Solicitudes" (Lista Unificada) ---
    List<SolicitudUnificadaDTO> listaUnificada = new ArrayList<>();

    // 1. Agregar CREACIONES (Nuevos Hechos)
    // Acá usamos 'todosMisHechos' para ver también los pendientes y rechazados
    for (HechoDTO h : todosMisHechos) {
      String estadoHecho = (h.estado() != null) ? h.estado() : "EN_REVISION";

      listaUnificada.add(new SolicitudUnificadaDTO(
          h.id(),
          h.titulo(),
          "Nuevo Hecho",
          estadoHecho
      ));
    }

    // 2. Agregar ELIMINACIONES (Bajas)
    for (SolicitudEliminacionOutputDTO baja : misBajas) {
      listaUnificada.add(new SolicitudUnificadaDTO(
          baja.nroDeSolicitud(),
          baja.tituloDelHechoAEliminar(),
          "Eliminación",
          baja.estado()
      ));
    }

    // 3. Agregar EDICIONES (Modificaciones)
    for (EdicionOutputDTO edi : misEdiciones) {
      // Si el título propuesto es null, usamos un texto genérico
      String tituloMostrar = (edi.getTituloPropuesto() != null)
          ? edi.getTituloPropuesto()
          : "Edición Hecho ID " + edi.getIdHechoOriginal();

      listaUnificada.add(new SolicitudUnificadaDTO(
          edi.getId(),
          tituloMostrar,
          "Edición",
          edi.getEstado()
      ));
    }

    model.addAttribute("misSolicitudes", listaUnificada);

    return "contributor";
  }
}