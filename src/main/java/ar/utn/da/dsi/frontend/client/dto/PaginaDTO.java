package ar.utn.da.dsi.frontend.client.dto;

import java.util.List;
import lombok.Data;

@Data
public class PaginaDTO<T> {
	private List<T> contenido;
	private int paginaActual;
	private int tamanioPagina;
	private long totalElementos;
	private int totalPaginas;
	private boolean esUltima;
}