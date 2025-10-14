package ar.utn.da.dsi.frontend.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Coleccion {
  private Long id;
  private String handle; // El nombre es diferente al DTO
  private String titulo;
  private String descripcion;
  private String consensusAlgorithm;
}