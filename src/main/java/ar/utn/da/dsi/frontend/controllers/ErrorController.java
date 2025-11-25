package ar.utn.da.dsi.frontend.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

  @GetMapping("/403")
  public String accessDenied(Model model) {
    model.addAttribute("titulo", "Acceso Denegado");
    return "error/403-access-denied";
  }
}