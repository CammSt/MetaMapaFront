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
						.requestMatchers("/login", "/registro", "/css/**", "/js/**", "/assets/**").permitAll()
						// (AÑADIDO) Permisos específicos (Ejemplo)
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/contributor/**").hasRole("CONTRIBUTOR")
						.requestMatchers("/hechos/nuevo").hasRole("CONTRIBUTOR")
						.anyRequest().authenticated()
				)
				.formLogin(form -> form
						.loginPage("/login")
						.usernameParameter("username")
						.passwordParameter("password")
						.permitAll()
						.successHandler(successHandler)
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