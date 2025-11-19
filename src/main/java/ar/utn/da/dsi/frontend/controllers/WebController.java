package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.client.dto.HechoDTO;
import ar.utn.da.dsi.frontend.client.dto.input.RegistroInputDTO;
import ar.utn.da.dsi.frontend.exceptions.ValidationException;
import ar.utn.da.dsi.frontend.services.colecciones.ColeccionService;
import ar.utn.da.dsi.frontend.services.hechos.HechoService;
import ar.utn.da.dsi.frontend.services.registro.RegistroService;
import ar.utn.da.dsi.frontend.services.solicitudes.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

  private final ColeccionService coleccionService;
  private final HechoService hechoService;
  private final RegistroService registroService;

  @Autowired
  public WebController(ColeccionService coleccionService, HechoService hechoService, SolicitudService solicitudService, RegistroService registroService) {
    this.coleccionService = coleccionService;
    this.hechoService = hechoService;
    this.registroService = registroService;
  }

  @GetMapping("/")
  public String index(Model model) {
    try {
      model.addAttribute("colecciones", coleccionService.obtenerTodas());
    } catch (Exception e) {
      model.addAttribute("colecciones", List.of());
    }
    return "index";
  }

  @GetMapping("/facts")
  public String verHechosDeColeccion(
      @RequestParam("handleId") String handleId,
      @RequestParam(required = false) String modo,
      @RequestParam(required = false) String fechaDesde,
      @RequestParam(required = false) String fechaHasta,
      @RequestParam(required = false) String categoria,
      @RequestParam(required = false) String titulo,
      Model model) {

    var coleccion = coleccionService.obtenerPorId(handleId);
    if (coleccion == null) return "error/404";

    model.addAttribute("coleccion", coleccion);

    //Llamamos al servicio con todos los filtros
    List<HechoDTO> hechosFiltrados = hechoService.getHechosDeColeccion(
        handleId, modo, fechaDesde, fechaHasta, categoria, titulo);

    model.addAttribute("hechos", hechosFiltrados);

    model.addAttribute("categorias", hechoService.getAvailableCategories());
    model.addAttribute("filtros", Map.of(
        "handleId", handleId,
        "modo", modo != null ? modo : "irrestricta",
        "fechaDesde", fechaDesde != null ? fechaDesde : "",
        "fechaHasta", fechaHasta != null ? fechaHasta : "",
        "categoria", categoria != null ? categoria : "",
        "titulo", titulo != null ? titulo : ""
    ));

    return "facts";
  }

  @GetMapping("/login")
  public String login() { return "login"; }

  @GetMapping("/profile")
  public String perfilUsuario() { return "profile"; }

  @GetMapping("/registro")
  public String mostrarFormularioRegistro(Model model) {
    if (!model.containsAttribute("registroDTO")) {
      model.addAttribute("registroDTO", new RegistroInputDTO());
    }
    return "registro";
  }

  @PostMapping("/registro")
  public String procesarRegistro(@ModelAttribute RegistroInputDTO registroDTO, RedirectAttributes redirectAttributes) {
    try {
      registroService.registrarUsuario(registroDTO);

      redirectAttributes.addFlashAttribute("success", "¡Registro exitoso! Por favor, inicia sesión.");
      return "redirect:/login";

    } catch (ValidationException e) {
      redirectAttributes.addFlashAttribute("error", "Error de validación: " + e.getFieldErrors().values().iterator().next());
      redirectAttributes.addFlashAttribute("registroDTO", registroDTO);
      return "redirect:/registro";
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error en el registro: " + e.getMessage());
      redirectAttributes.addFlashAttribute("registroDTO", registroDTO);
      return "redirect:/registro";
    }
  }

  @GetMapping("/login-success")
  public String loginSuccess(@RequestParam("userRole") String userRole, Model model) {
    model.addAttribute("userRole", userRole);
    return "login-success";
  }
}