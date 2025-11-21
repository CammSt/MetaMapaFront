package ar.utn.da.dsi.frontend.client.dto.output;

public record ProvinciaHechosPorCategoriaDTO(
    String provincia,
    String categoria,
    Long cantidadHechos
) {}