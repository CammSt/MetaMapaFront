package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.services.ColeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Controller
@RequestMapping("/colecciones")
@RequiredArgsConstructor
public class ColeccionController {
  private static final Logger log = LoggerFactory.getLogger(ColeccionController.class);
  private final ColeccionService coleccionService;

  @GetMapping
  public String listarColecciones(Model model, Authentication authentication) {
    List<ColeccionDTO> alumnos = coleccionService.obtenerTodasLasColecciones();
    model.addAttribute("alumnos", alumnos);
    model.addAttribute("titulo", "Listado de alumnos");
    model.addAttribute("totalDeAlumnos", alumnos.size());
    model.addAttribute("usuario", authentication.getName());
    return "alumnos/lista";
  }
}
