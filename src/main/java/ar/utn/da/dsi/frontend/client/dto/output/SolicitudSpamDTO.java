package ar.utn.da.dsi.frontend.client.dto.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // <--- Genera Getters y Setters automáticamente
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudSpamDTO {

  // El porcentaje de spam calculado
  private Double porcentaje;

  // CORRECCIÓN:
  // El backend envía este dato como "cantidad" en el JSON.
  // Nosotros queremos usarlo como "totalSolicitudes" en Java/HTML.
  // Esta anotación hace la traducción automática.
  @JsonProperty("cantidad")
  private Long totalSolicitudes;
}