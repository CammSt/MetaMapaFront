package ar.utn.da.dsi.frontend.client.dto;
import lombok.Data;
import ar.utn.da.dsi.frontend.client.dto.RolesPermisosDTO;
import java.time.LocalDate;

@Data
public class AuthResponseDTO {
	private String token;
	private RolesPermisosDTO rolesPermisos;
	private Long id;
	private String nombre;
	private String apellido;
	private String email;
	private LocalDate fechaDeNacimiento;
}