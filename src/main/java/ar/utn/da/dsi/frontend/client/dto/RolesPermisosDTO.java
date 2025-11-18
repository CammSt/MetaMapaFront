package ar.utn.da.dsi.frontend.client.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolesPermisosDTO {
	private String email;
	private String nombreRol;
	private List<String> permisos;
}