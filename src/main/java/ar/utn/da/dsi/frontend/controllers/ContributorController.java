package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.services.hechos.HechoService;
import ar.utn.da.dsi.frontend.services.solicitudes.SolicitudService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.ArrayList;

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
  }
}
