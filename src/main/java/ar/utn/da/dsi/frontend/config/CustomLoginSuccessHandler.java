package ar.utn.da.dsi.frontend.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;

  @Component
  public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    String userRole = "contributor";

    // Verificamos los roles (autoridades) del usuario autenticado
      for (GrantedAuthority auth : authentication.getAuthorities()) {
      if (auth.getAuthority().equals("ROLE_ADMIN")) {
        userRole = "admin";
        break;
      }
    }

    String targetUrl = UriComponentsBuilder.fromPath("/login-success")
        .queryParam("userRole", userRole)
        .build()
        .toUriString();

      response.sendRedirect(targetUrl);
  }
}