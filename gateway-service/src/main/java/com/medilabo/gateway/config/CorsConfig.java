package com.medilabo.gateway.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * CORS configuration for the API Gateway.
 *
 * This configuration allows the Angular frontend application to communicate
 * with the gateway by enabling Cross-Origin Resource Sharing (CORS).
 *
 * The gateway is the only component responsible for handling CORS, keeping the
 * downstream microservices independent from frontend concerns.
 */
@Configuration
public class CorsConfig {
	/**
	 * Defines a global CORS filter for the gateway.
	 *
	 * This configuration:
	 * <ul>
	 * Allows requests from the Angular frontend (localhost:4200)
	 * Supports common HTTP methods used by the API
	 * Allows all request headers, including Authorization
	 * </ul>
	 *
	 * @return a configured {@link CorsWebFilter}
	 */
	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration c = new CorsConfiguration();
		c.setAllowCredentials(true);
		c.setAllowedOrigins(List.of("http://localhost:4200"));
		c.setAllowedHeaders(List.of("*"));
		c.setAllowedMethods(List.of("GET", "POST", "PUT", "OPTIONS"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", c);
		return new CorsWebFilter(source);
	}
}