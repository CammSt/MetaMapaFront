package ar.utn.da.dsi.frontend.providers;

import ar.utn.da.dsi.frontend.client.dto.AuthResponseDTO;
import ar.utn.da.dsi.frontend.client.dto.RolesPermisosDTO;
import ar.utn.da.dsi.frontend.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthProvider implements AuthenticationProvider {
	private static final Logger log = LoggerFactory.getLogger(CustomAuthProvider.class);
	private final AuthService externalAuthService;

	public CustomAuthProvider(AuthService externalAuthService) {
		this.externalAuthService = externalAuthService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = authentication.getCredentials().toString();

		try {
			// Llamar a login (que ahora devuelve TODOS los datos)
			AuthResponseDTO authResponse = externalAuthService.login(username, password);
			if (authResponse == null) {
				throw new BadCredentialsException("Usuario o contraseña inválidos");
			}

			String accessToken = authResponse.getToken();
			String rolBackend = authResponse.getRolesPermisos().getNombreRol();
			List<String> permisos = authResponse.getRolesPermisos().getPermisos();

			//Determinar el rol
			String feRole = rolBackend.equalsIgnoreCase("ADMIN") ? "admin" : "contributor";

			//CONSTRUIR OBJETO JSON COMPLETO Y DINÁMICO para sessionStorage
			String userJson = String.format(
					"{\"id\":%d,\"name\":\"%s\",\"lastName\":\"%s\",\"role\":\"%s\",\"email\":\"%s\",\"birthDate\":\"%s\"}",
					authResponse.getId(),
					authResponse.getNombre().replace("\"", "\\\""),
					authResponse.getApellido().replace("\"", "\\\""),
					feRole,
					authResponse.getEmail(),
					authResponse.getFechaDeNacimiento().toString()
			);

			//GUARDAR EN SESIÓN HTTP
			log.info("Usuario logeado! Configurando variables de sesión");
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpSession session = attributes.getRequest().getSession(true);

			session.setAttribute("accessToken", accessToken);
			session.setAttribute("userJson", userJson);
			session.setAttribute("userRole", feRole);

			//CONSTRUIR AUTHORITIES para Spring Security
			List<GrantedAuthority> authorities = new ArrayList<>();
			permisos.forEach(permisoString -> {
				authorities.add(new SimpleGrantedAuthority(permisoString));
			});
			authorities.add(new SimpleGrantedAuthority("ROLE_" + rolBackend));

			return new UsernamePasswordAuthenticationToken(username, password, authorities);

		} catch (RuntimeException e) {
			log.error("Error en el sistema de autenticación: {}", e.getMessage());
			throw new BadCredentialsException("Error en el sistema de autenticación: " + e.getMessage());
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}