package ar.utn.da.dsi.frontend.client.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class RolUsuarioDTO {
  private Integer id;
  private String nombrePermiso;
}