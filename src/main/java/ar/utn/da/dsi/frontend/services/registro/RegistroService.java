package ar.utn.da.dsi.frontend.services.registro;

import ar.utn.da.dsi.frontend.client.dto.AuthResponseDTO;
import ar.utn.da.dsi.frontend.client.dto.input.RegistroInputDTO;
import ar.utn.da.dsi.frontend.exceptions.ValidationException;
import ar.utn.da.dsi.frontend.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistroService {

  private final AuthService authApiService;

  @Autowired
  public RegistroService(AuthService authApiService) {
    this.authApiService = authApiService;
  }

  public AuthResponseDTO registrarUsuario(RegistroInputDTO dto) {
    validarRegistro(dto);

    return authApiService.registrar(dto);
  }

  private void validarRegistro(RegistroInputDTO dto) {
    ValidationException validationException = new ValidationException("Errores de validación");

    if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
      validationException.addFieldError("nombre", "El nombre es obligatorio");
    }
    if (dto.getEmail() == null || !dto.getEmail().contains("@")) {
      validationException.addFieldError("email", "El email no es válido");
    }
    if (dto.getPassword() == null || dto.getPassword().length() < 6) {
      validationException.addFieldError("password", "La contraseña debe tener al menos 6 caracteres");
    }
    if (!dto.getPassword().equals(dto.getConfirmarPassword())) {
      validationException.addFieldError("confirmarPassword", "Las contraseñas no coinciden");
    }
    if (dto.getFechaNacimiento() == null) {
      validationException.addFieldError("fechaNacimiento", "La fecha de nacimiento es obligatoria");
    }

    if (validationException.hasFieldErrors()) {
      throw validationException;
    }
  }
}