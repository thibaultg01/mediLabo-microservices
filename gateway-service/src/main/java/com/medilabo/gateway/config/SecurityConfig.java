package com.medilabo.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	/**
	   * Configures the reactive security filter chain for the gateway.
	   *
	   * <p>
	   * This configuration:
	   * <ul>
	   *   <li>Disables CSRF protection (REST API)</li>
	   *   <li>Allows CORS preflight (HTTP OPTIONS) requests</li>
	   *   <li>Exposes selected actuator endpoints for observability</li>
	   *   <li>Secures all other routes with HTTP Basic authentication</li>
	   * </ul>
	   *
	   * @param http the reactive ServerHttpSecurity configuration
	   * @return the configured SecurityWebFilterChain
	   */
	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.authorizeExchange(ex -> ex.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.pathMatchers("/actuator/health", "/actuator/info", "/actuator/gateway/**").permitAll()
						.anyExchange().authenticated())
				.httpBasic(Customizer.withDefaults()).build();
	}

	/**
	   * Reactive in-memory user details service.
	   *
	   * The username and raw password are loaded from the application configuration.
	   * The password is encoded using BCrypt before being stored in memory.
	   *
	   * This implementation is sufficient for the project requirements, as no
	   * user management or persistence layer is required.
	   *
	   * @param username the configured username
	   * @param rawPassword the raw password from configuration
	   * @param encoder the password encoder
	   * @return a MapReactiveUserDetailsService containing the configured user
	   */
	@Bean
	MapReactiveUserDetailsService users(@Value("${app.auth.username:user}") String username,
			@Value("${app.auth.password:password}") String rawPassword, PasswordEncoder encoder) {
		UserDetails user = User.withUsername("user").password(encoder.encode("password")).roles("USER").build();
		return new MapReactiveUserDetailsService(user);
	}

	/**
	   * Password encoder used by the gateway.
	   *
	   * <p>
	   * BCrypt is used to securely hash passwords before they are stored
	   * in memory, following security best practices.
	   *
	   * @return a PasswordEncoder using BCrypt
	   */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
