package com.medilabo.gateway.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
  @Bean
  public CorsWebFilter corsWebFilter() {
    CorsConfiguration c = new CorsConfiguration();
    c.setAllowCredentials(true);
    c.setAllowedOrigins(List.of("http://localhost:4200"));
    c.setAllowedHeaders(List.of("*"));
    c.setAllowedMethods(List.of("GET","POST","PUT","OPTIONS"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", c);
    return new CorsWebFilter(source);
  }
}