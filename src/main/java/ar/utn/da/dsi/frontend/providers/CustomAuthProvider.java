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
			//Llamar a login
			AuthResponseDTO authResponse = externalAuthService.login(username, password);
			if (authResponse == null) {
				throw new BadCredentialsException("Usuario o contraseña inválidos");
			}

			//Guardar tokens en sesión
			log.info("Usuario logeado! Configurando variables de sesión");
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			HttpServletRequest request = attributes.getRequest();

			request.getSession().setAttribute("accessToken", authResponse.getToken());
			request.getSession().setAttribute("username", username);

			//Obtener roles y permisos
			log.info("Buscando roles y permisos del usuario");
			RolesPermisosDTO rolesPermisos = externalAuthService.getRolesPermisos(authResponse.getToken());

			//Guardar roles en sesión
			log.info("Cargando roles y permisos del usuario en sesión");
			// (CORREGIDO) Usamos el getter del DTO (String)
			request.getSession().setAttribute("rol", rolesPermisos.getNombreRol());
			request.getSession().setAttribute("permisos", rolesPermisos.getPermisos());

			List<GrantedAuthority> authorities = new ArrayList<>();
			rolesPermisos.getPermisos().forEach(permisoString -> {
				authorities.add(new SimpleGrantedAuthority(permisoString));
			});
			authorities.add(new SimpleGrantedAuthority("ROLE_" + rolesPermisos.getNombreRol()));

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