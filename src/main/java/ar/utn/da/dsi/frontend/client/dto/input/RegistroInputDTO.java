package ar.utn.da.dsi.frontend.client.dto.input;

import lombok.Data;
import java.time.LocalDate;

@Data
public class RegistroInputDTO {
  private String nombre;
  private LocalDate fechaNacimiento;
  private String email; // 'username' en el backend
  private String password;
  private String confirmarPassword;
}