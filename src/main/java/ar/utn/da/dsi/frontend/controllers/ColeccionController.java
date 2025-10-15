package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.client.dto.output.ColeccionOutputDTO;
import ar.utn.da.dsi.frontend.exceptions.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ar.utn.da.dsi.frontend.client.dto.input.ColeccionInputDTO;
import ar.utn.da.dsi.frontend.exceptions.ValidationException;
import ar.utn.da.dsi.frontend.services.ColeccionService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/colecciones")
public class ColeccionController {
  private final ColeccionService coleccionService;

	public ColeccionController(ColeccionService coleccionService) {
		this.coleccionService = coleccionService;
	}

  @GetMapping
  public String listarColecciones(Model model) {
    model.addAttribute("colecciones", coleccionService.obtenerTodas());
    model.addAttribute("titulo", "Listado de Colecciones");
    return "colecciones/lista";
  }

  @GetMapping("/nuevo")
  public String mostrarFormularioCrear(Model model) {
    // Enviamos el DTO de ENTRADA al formulario
    model.addAttribute("coleccion", new ColeccionInputDTO());
    model.addAttribute("titulo", "Crear Nueva Colección");
    return "colecciones/crear";
  }

  @PostMapping("/crear")
  public String crearColeccion(@ModelAttribute("coleccion") ColeccionInputDTO dto, BindingResult bindingResult, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
    try {
      dto.setVisualizadorID(authentication.getName());

      coleccionService.crear(dto);
      redirectAttributes.addFlashAttribute("mensaje_exito", "Colección creada exitosamente.");
      return "redirect:/colecciones";
    } catch (ValidationException e) {
      convertirValidationExceptionABindingResult(e, bindingResult);
      model.addAttribute("titulo", "Crear Nueva Colección");
      return "colecciones/crear";
    } catch (Exception e) {
      model.addAttribute("mensaje_error", "Error inesperado al crear la colección: " + e.getMessage());
      model.addAttribute("titulo", "Crear Nueva Colección");
      return "colecciones/crear";
    }
  }

  @GetMapping("/{id}/editar")
  public String mostrarFormularioEditar(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
    try {
      // 1. Obtenemos los datos de la colección para mostrar
      ColeccionOutputDTO coleccionExistente = coleccionService.obtenerPorId(id);

      // 2. Mapeamos los datos de salida (Output) a un DTO de entrada (Input) para el formulario
      ColeccionInputDTO coleccionParaForm = new ColeccionInputDTO();
      coleccionParaForm.setHandleID(coleccionExistente.getHandleID());
      coleccionParaForm.setTitulo(coleccionExistente.getTitulo());
      coleccionParaForm.setDescripcion(coleccionExistente.getDescripcion());
      // El algoritmo no se puede obtener del DTO de salida, se debería manejar en el form si se quiere mostrar

      model.addAttribute("coleccion", coleccionParaForm);
      model.addAttribute("titulo", "Editar Colección");
      return "colecciones/editar";
    } catch (NotFoundException e) {
      redirectAttributes.addFlashAttribute("mensaje_error", "Colección no encontrada.");
      return "redirect:/colecciones";
    }
  }

  @PostMapping("/{id}/actualizar")
  public String actualizarColeccion(@PathVariable String id, @ModelAttribute("coleccion") ColeccionInputDTO dto, BindingResult bindingResult, Model model, Authentication authentication, RedirectAttributes redirectAttributes) {
    try {
      dto.setVisualizadorID(authentication.getName());
      coleccionService.actualizar(id, dto);
      redirectAttributes.addFlashAttribute("mensaje_exito", "Colección actualizada exitosamente.");
      return "redirect:/colecciones";
    } catch (ValidationException e) {
      convertirValidationExceptionABindingResult(e, bindingResult);
      model.addAttribute("titulo", "Editar Colección");
      return "colecciones/editar";
    } catch (NotFoundException e) {
      redirectAttributes.addFlashAttribute("mensaje_error", "Colección no encontrada al intentar actualizar.");
      return "redirect:/colecciones";
    } catch (Exception e) {
      model.addAttribute("mensaje_error", "Error inesperado: " + e.getMessage());
      model.addAttribute("titulo", "Editar Colección");
      return "colecciones/editar";
    }
  }

  @PostMapping("/{id}/eliminar")
  public String eliminarColeccion(@PathVariable String id, Authentication authentication, RedirectAttributes redirectAttributes) {
    try {
      String visualizadorID = authentication.getName();
      coleccionService.eliminar(id, visualizadorID);
      redirectAttributes.addFlashAttribute("mensaje_exito", "Colección eliminada exitosamente.");
    } catch (NotFoundException e) {
      redirectAttributes.addFlashAttribute("mensaje_error", "No se encontró la colección a eliminar.");
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("mensaje_error", "Error al eliminar la colección: " + e.getMessage());
    }
    return "redirect:/colecciones";
  }

  private void convertirValidationExceptionABindingResult(ValidationException e, BindingResult bindingResult) {
    if (e.hasFieldErrors()) {
      e.getFieldErrors().forEach((field, error) -> bindingResult.rejectValue(field, "error." + field, error));
    }
  }

}
