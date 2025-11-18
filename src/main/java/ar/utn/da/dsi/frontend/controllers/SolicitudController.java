package ar.utn.da.dsi.frontend.controllers;

import ar.utn.da.dsi.frontend.client.dto.input.HechoInputDTO;
import ar.utn.da.dsi.frontend.client.dto.input.SolicitudEliminacionInputDTO;
import ar.utn.da.dsi.frontend.services.hechos.HechoService;
import ar.utn.da.dsi.frontend.services.solicitudes.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.lang.Nullable;

@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {

  private final SolicitudService solicitudService;
  private final HechoService hechoService;

  @Autowired
  public SolicitudController(SolicitudService solicitudService, HechoService hechoService) {
    this.solicitudService = solicitudService;
    this.hechoService = hechoService;
  }

  @GetMapping("/nueva")
  public String mostrarFormularioNuevaSolicitud(@RequestParam("hechoId") Long hechoId, Model model) {

    HechoInputDTO hechoDTO = hechoService.getHechoInputDTOporId(hechoId);

    SolicitudEliminacionInputDTO solicitudDTO = new SolicitudEliminacionInputDTO();

    solicitudDTO.setTituloHecho(hechoDTO.getTitulo());

    model.addAttribute("hecho", hechoDTO);
    model.addAttribute("solicitudDTO", solicitudDTO);
    model.addAttribute("titulo", "Solicitar Eliminación de Hecho");

    return "solicitud-form";
  }

  @PostMapping("/crear")
  public String crearSolicitud(@ModelAttribute SolicitudEliminacionInputDTO solicitudDTO, @Nullable Authentication auth) {

    solicitudService.crear(solicitudDTO);

    return "redirect:/?success=solicitud_creada";
  }
}