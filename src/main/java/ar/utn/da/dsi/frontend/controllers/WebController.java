package ar.utn.da.dsi.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

  // Maneja las peticiones a la página de inicio ("/")
  @GetMapping("/")
  public String index(Model model) {
    model.addAttribute("pageTitle", "Inicio");
    // Devuelve el nombre del archivo HTML (sin la extensión .html)
    return "index";
  }

  // Maneja las peticiones a la página de login ("/login")
  @GetMapping("/login")
  public String login(Model model) {
    model.addAttribute("pageTitle", "Acceso");
    return "login";
  }

  // Maneja las peticiones a la página del administrador ("/admin")
  @GetMapping("/admin")
  public String adminPanel(Model model) {
    model.addAttribute("pageTitle", "Administración");
    return "admin";
  }

  // Maneja las peticiones a la página de hechos ("/facts")
  @GetMapping("/facts")
  public String facts(Model model) {
    model.addAttribute("pageTitle", "Hechos de la Colección");
    return "facts";
  }

  // Maneja las peticiones a la página del contribuyente ("/contributor")
  @GetMapping("/contributor")
  public String contributorPanel(Model model) {
    model.addAttribute("pageTitle", "Mi Panel");
    return "contributor";
  }

  // Maneja las peticiones a la página de perfil de usuario ("/profile")
  @GetMapping("/profile")
  public String userProfile(Model model) {
    model.addAttribute("pageTitle", "Mi Perfil");
    return "profile";
  }
}