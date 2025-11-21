package ar.utn.da.dsi.frontend.client.dto.output;

public record HoraHechosPorCategoriaDTO(
    Integer hora,
    String categoria,
    Long cantidadHechos
) {}