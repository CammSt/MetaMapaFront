package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.services.solicitudes.SolicitudService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contributor")
public class ContributorController {

  private final SolicitudService solicitudService;

  public ContributorController(SolicitudService solicitudService) {
    this.solicitudService = solicitudService;
  }

  @GetMapping
  public String mostrarPanelContribuyente(Model model, Authentication authentication) {
    String userId = authentication.getName();

    model.addAttribute("misHechos", hechoService.buscarHechosPorUsuario(userId));
    model.addAttribute("misSolicitudes", solicitudService.obtenerTodas(userId));

    return "contributor";
  }
}
