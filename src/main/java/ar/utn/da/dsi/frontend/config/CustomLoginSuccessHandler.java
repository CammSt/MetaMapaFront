package ar.utn.da.dsi.frontend.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {

    // Verificamos los roles (autoridades) del usuario autenticado
    for (GrantedAuthority auth : authentication.getAuthorities()) {
      if (auth.getAuthority().equals("ROLE_ADMIN")) {
        // Si es ADMIN, lo mandamos al panel de admin
        response.sendRedirect("/admin");
        return;
      }
    }

    // Si no es ADMIN (es CONTRIBUTOR o cualquier otro), lo mandamos al inicio
    response.sendRedirect("/");
  }
}