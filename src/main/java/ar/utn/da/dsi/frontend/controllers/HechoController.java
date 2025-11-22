package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.client.dto.input.HechoInputDTO;
import ar.utn.da.dsi.frontend.services.hechos.HechoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

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
    try {
      model.addAttribute("categorias", hechoService.getAvailableCategories());
    } catch (Exception e) {
      model.addAttribute("categorias", List.of());
    }

    return "hecho-form";
  }

  @PostMapping("/crear")
  public String crearHecho(
      @ModelAttribute HechoInputDTO hechoDTO,
      @RequestParam(value = "archivo", required = false) MultipartFile archivo,
      @Nullable Authentication auth) {
    String userId = null;
    if (auth != null) {
      userId = auth.getName();
    }

    hechoDTO.setVisualizadorID(userId);

    hechoService.crear(hechoDTO, archivo);

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
  public String editarHecho(
      @PathVariable("id") Long id,
      @ModelAttribute HechoInputDTO hechoDTO,
      @RequestParam(value = "archivo", required = false) MultipartFile archivo,
      Authentication auth) {

    String userId = auth.getName();
    hechoDTO.setVisualizadorID(userId);

    hechoService.actualizar(id, hechoDTO, archivo);

    return "redirect:/contributor?success=hecho_editado";
  }

}