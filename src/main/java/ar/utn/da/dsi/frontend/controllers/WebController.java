package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.services.colecciones.ColeccionService;
import ar.utn.da.dsi.frontend.services.hechos.HechoService;
import ar.utn.da.dsi.frontend.services.solicitudes.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class WebController {

  private final ColeccionService coleccionService;
  private final HechoService hechoService;

  @Autowired
  public WebController(ColeccionService coleccionService, HechoService hechoService, SolicitudService solicitudService) {
    this.coleccionService = coleccionService;
    this.hechoService = hechoService;
  }

  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("colecciones", coleccionService.obtenerTodas());
    return "index";
  }

  @GetMapping("/facts")
  public String verHechosDeColeccion(@RequestParam("handleId") String handleId, Model model) {
    var coleccion = coleccionService.obtenerPorId(handleId);
    if (coleccion == null) return "error/4404"; // Deberíamos tener una vista 404

    model.addAttribute("coleccion", coleccion);

    model.addAttribute("hechos", hechoService.getHechosDeColeccion(handleId));

    model.addAttribute("categorias", hechoService.getAvailableCategories());

    return "facts";
  }

  @GetMapping("/login")
  public String login() { return "login"; }

  // NOTA: Este método de login sigue hardcodeado,
  // pero lo estamos arreglando al usar CustomAuthProvider
  @PostMapping("/login")
  public RedirectView handleLogin(@RequestParam String email, @RequestParam String password) {
    if ("admin@test".equals(email) && "admin".equals(password)) {
      return new RedirectView("/login-success?role=admin");
    } else if ("user@test".equals(email) && "user".equals(password)) {
      return new RedirectView("/login-success?role=contributor");
    } else {
      return new RedirectView("/login?error=true");
    }
  }

  @GetMapping("/login-success")
  public String loginSuccess(@RequestParam String role, Model model) {
    model.addAttribute("userRole", role);
    return "login-success";
  }

  @GetMapping("/profile")
  public String perfilUsuario() { return "profile"; }
}