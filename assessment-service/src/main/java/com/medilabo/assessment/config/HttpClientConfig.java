package com.medilabo.assessment.config;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientConfig {
  @Bean
  RestClient restClient(
      @Value("${app.auth.username}") String user,
      @Value("${app.auth.password}") String pass) {

    String token = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes());
    return RestClient.builder()
        .defaultHeader("Authorization", "Basic " + token)
        .defaultHeader("Accept", "application/json")
        .build();
  }
}