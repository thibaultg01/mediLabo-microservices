package com.medilabo.assessment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class SecurityConfig {
	/**
     * Configures the Spring Security filter chain.
     *
     * - Disables CSRF protection (not needed for stateless REST APIs)
     * - Disables HTTP session creation (STATELESS)
     * - Exposes actuator health and info endpoints without authentication
     * - Secures all other endpoints using HTTP Basic authentication
     *
     * @param http the HttpSecurity configuration object
     * @return the configured SecurityFilterChain
     * @throws Exception if a security configuration error occurs
     */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/actuator/health", "/actuator/info").permitAll()
						.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults());

		return http.build();
	}

	/**
     * Password encoder used by Spring Security.
     *
     * <p>
     * BCrypt is used to securely hash user passwords before storing them
     * in memory. This follows recommended security practices.
     *
     * @return a PasswordEncoder using BCrypt
     */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
     * In-memory user details service.
     *
     * The username and raw password are read from application configuration.
     * The password is encoded at startup using the configured PasswordEncoder.
     *
     * This service is sufficient for the project needs, as no user management
     * or persistence is required.
     *
     * @param username the username from configuration
     * @param rawPassword the raw password from configuration
     * @param encoder the password encoder
     * @return a configured UserDetailsService
     */
	@Bean
	UserDetailsService userDetailsService(@Value("${app.auth.username}") String username,
			@Value("${app.auth.password}") String rawPassword, PasswordEncoder encoder) {
		UserDetails user = User.withUsername(username).password(encoder.encode(rawPassword))
				.roles("USER").build();

		return new InMemoryUserDetailsManager(user);
	}
}
