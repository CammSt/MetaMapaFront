package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.client.MetaMapaApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class WebController {

  @Autowired
  private MetaMapaApiClient apiClient;

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("colecciones", apiClient.getColecciones());
    return "index";
  }

  @GetMapping("/facts")
  public String verHechosDeColeccion(@RequestParam("handleId") String handleId, Model model) {
    var coleccion = apiClient.getColeccion(handleId);
    if (coleccion == null) return "error/404";

    model.addAttribute("coleccion", coleccion);
    model.addAttribute("hechos", apiClient.getHechosDeColeccion(handleId));
    model.addAttribute("categorias", apiClient.getAvailableCategories());

    return "facts";
  }

  @GetMapping("/login")
  public String login() { return "login"; }

  @PostMapping("/login")
  public RedirectView handleLogin(@RequestParam String email, @RequestParam String password) {
    // --- LÓGICA DE AUTENTICACIÓN HARDCODEADA ---

    // 1. Verifica si las credenciales son de Administrador
    if ("admin@test".equals(email) && "admin".equals(password)) {
      return new RedirectView("/login-success?role=admin");
    }

    // 2. Verifica si las credenciales son de Contribuyente
    else if ("user@test".equals(email) && "user".equals(password)) {
      return new RedirectView("/login-success?role=contributor");
    }

    // 3. Si no coincide ninguna, vuelve al login
    else {
      return new RedirectView("/login?error=true");
    }
  }

  @GetMapping("/login-success")
  public String loginSuccess(@RequestParam String role, Model model) {
    model.addAttribute("userRole", role);
    return "login-success"; // Renderiza el nuevo archivo HTML
  }


  @GetMapping("/admin")
  public String panelAdmin(Model model) {
    model.addAttribute("colecciones", apiClient.getColecciones());
    model.addAttribute("solicitudes", apiClient.getSolicitudes());
    model.addAttribute("consensusLabels", apiClient.getConsensusLabels());
    model.addAttribute("availableSources", apiClient.getAvailableSources());

    return "admin";
  }

  @GetMapping("/contributor")
  public String panelContribuyente(Model model) {
    Long currentUserId = 1L; // Simulación
    model.addAttribute("misHechos", apiClient.getHechosPorUsuario(currentUserId));
    model.addAttribute("misSolicitudes", apiClient.getSolicitudesPorUsuario(currentUserId));
    return "contributor";
  }

  @GetMapping("/profile")
  public String perfilUsuario() { return "profile"; }
}