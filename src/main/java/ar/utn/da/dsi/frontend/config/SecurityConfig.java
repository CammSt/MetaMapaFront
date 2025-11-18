package ar.utn.da.dsi.frontend.config;

import ar.utn.da.dsi.frontend.providers.CustomAuthProvider;
import org.springframework.beans.factory.annotation.Autowired; // (AÑADIR)
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler; // (AÑADIR)

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

	@Autowired
	private AuthenticationSuccessHandler successHandler;

	@Bean
	public AuthenticationManager authManager(HttpSecurity http, CustomAuthProvider provider) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.authenticationProvider(provider)
				.build();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth

						//RUTAS PÚBLICAS (Permitir a TODOS)
						.requestMatchers(
								"/", "/facts",                   // Páginas de visualización
								"/login", "/registro",          // Login y Registro
								"/hechos/nuevo", "/hechos/crear",      // Enviar Hecho (Anónimo)
								"/solicitudes/nueva", "/solicitudes/crear", // Enviar Solicitud (Anónimo)
								"/css/**", "/js/**", "/assets/**" // Recursos estáticos
						).permitAll()

						//RUTAS DE CONTRIBUYENTE (Requieren estar registrados)
						.requestMatchers(
								"/contributor/**",              // Panel de Contribuyente
								"/hechos/{id}/editar",        // Formulario de Edición
								"/profile"                      // Ver perfil
						).hasRole("CONTRIBUTOR") // Spring añade "ROLE_" automáticamente

						//RUTAS DE ADMIN (Requieren ser ADMIN)
						.requestMatchers("/admin/**").hasRole("ADMIN")

						// OTRAS RUTAS
						.anyRequest().authenticated() // Bloquea todo lo demás
				)
				.formLogin(form -> form
						.loginPage("/login")
						.usernameParameter("username")
						.passwordParameter("password")
						.permitAll()
						.successHandler(successHandler) // Redirección por rol
				)
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login?logout")
						.permitAll()
				)
				.exceptionHandling(ex -> ex
						.authenticationEntryPoint((request, response, authException) ->
								response.sendRedirect("/login?unauthorized")
						)
				);

		return http.build();
	}
}