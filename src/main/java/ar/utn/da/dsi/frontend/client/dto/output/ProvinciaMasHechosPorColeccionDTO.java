package ar.utn.da.dsi.frontend.client.dto.output;

public record ProvinciaMasHechosPorColeccionDTO(
    String provincia,
    String handleID,
    Long cantidadHechos
) {}