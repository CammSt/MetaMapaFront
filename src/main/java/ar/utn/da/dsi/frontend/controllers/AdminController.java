package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.services.colecciones.ColeccionService; // Tu servicio de frontend
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ar.utn.da.dsi.frontend.services.solicitudes.SolicitudService;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin") // Cambiamos la ruta para que coincida con la de tu sidebar
public class AdminController { // Renombrado para mayor claridad

  private final ColeccionService coleccionService;
  private final SolicitudService solicitudService; // Servicio para las solicitudes

  public AdminController(ColeccionService coleccionService, SolicitudService solicitudService) {
    this.coleccionService = coleccionService;
    this.solicitudService = solicitudService;
  }

  @GetMapping
  public String mostrarPanelAdmin(Model model, Authentication authentication) {
    // Obtenemos los datos necesarios para las dos pestañas de la página de admin
    model.addAttribute("colecciones", coleccionService.obtenerTodas());

    String adminId = (authentication != null) ? authentication.getName() : null;
    if (adminId != null) {
      model.addAttribute("solicitudes", solicitudService.obtenerTodas(adminId));
    } else {
      model.addAttribute("solicitudes", List.of()); // Si no hay usuario, lista vacía
    }

    // Datos para los modales (los que pasabas vía Thymeleaf en admin.html)
    model.addAttribute("consensusLabels", Map.of("none", "No Especificado", "multiple", "Múltiples Menciones"));
    model.addAttribute("availableSources", List.of("Carga Manual", "API Sudoeste"));

    model.addAttribute("titulo", "Panel de Administración");
    return "admin"; // Devuelve la vista: templates/admin.html
  }
}