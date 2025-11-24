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
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

  @Component
  public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

      HttpSession session = request.getSession(false);
      if (session == null) {
        response.sendRedirect("/");
        return;
      }

      String userJson = (String) session.getAttribute("userJson");
      String userRole = (String) session.getAttribute("userRole");
      String accessToken = (String) session.getAttribute("accessToken");

      if (userJson == null || userRole == null || accessToken == null) {
        response.sendRedirect("/");
        return;
      }

      //CODIFICAR EL TOKEN
      String encodedUserJson = URLEncoder.encode(userJson, StandardCharsets.UTF_8);
      String encodedAccessToken = URLEncoder.encode(accessToken, StandardCharsets.UTF_8);

      //Redirigir, AÑADIENDO EL TOKEN
      String targetUrl = UriComponentsBuilder.fromPath("/login-success")
          .queryParam("userJson", encodedUserJson)
          .queryParam("userRole", userRole)
          .queryParam("accessToken", encodedAccessToken)
          .build()
          .toUriString();

      response.sendRedirect(targetUrl);
    }
}