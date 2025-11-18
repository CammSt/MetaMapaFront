package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.client.dto.input.HechoInputDTO;
import ar.utn.da.dsi.frontend.services.hechos.HechoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.Nullable;

@Controller
@RequestMapping("/hechos")
public class HechoController {

  private final HechoService hechoService;

  @Autowired
  public HechoController(HechoService hechoService) {
    this.hechoService = hechoService;
  }

  @GetMapping("/nuevo")
  public String mostrarFormularioNuevoHecho(Model model) {
    model.addAttribute("hechoDTO", new HechoInputDTO());
    model.addAttribute("titulo", "Subir un nuevo Hecho");
    model.addAttribute("accion", "crear");

    model.addAttribute("categorias", hechoService.getAvailableCategories());

    return "hecho-form";
  }

  @PostMapping("/crear")
  public String crearHecho(@ModelAttribute HechoInputDTO hechoDTO, @Nullable Authentication auth) {
    String userId = null;
    if (auth != null) {
      userId = auth.getName();
    }

    // El backend debe estar preparado para aceptar un visualizadorID nulo (anónimo).
    hechoDTO.setVisualizadorID(userId);

    hechoService.crear(hechoDTO);

    return "redirect:/?success=hecho_creado";
  }

  @GetMapping("/{id}/editar")
  public String mostrarFormularioEditarHecho(@PathVariable("id") Long id, Model model) {

    HechoInputDTO hechoDTO = hechoService.getHechoInputDTOporId(id);

    model.addAttribute("hechoDTO", hechoDTO);
    model.addAttribute("titulo", "Editar Hecho: " + hechoDTO.getTitulo());
    model.addAttribute("accion", "editar");

    model.addAttribute("categorias", hechoService.getAvailableCategories());

    return "hecho-form";
  }

  @PostMapping("/{id}/editar")
  public String editarHecho(@PathVariable("id") Long id,
                            @ModelAttribute HechoInputDTO hechoDTO,
                            Authentication auth) {

    String userId = auth.getName();
    hechoDTO.setVisualizadorID(userId);

    hechoService.actualizar(id, hechoDTO);

    return "redirect:/contributor?success=hecho_editado";
  }
}