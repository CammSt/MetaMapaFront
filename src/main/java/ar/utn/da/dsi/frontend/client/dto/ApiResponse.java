package ar.utn.da.dsi.frontend.client.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
	private int codigo;
	private String estado;
	private String mensaje;
	private T datos;
}