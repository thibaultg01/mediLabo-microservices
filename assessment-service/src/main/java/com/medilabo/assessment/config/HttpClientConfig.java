package com.medilabo.assessment.config;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class HttpClientConfig {
	/**
     * Creates a RestClient configured with HTTP Basic authentication.
     *
     * The Authorization header is generated once at startup using the
     * configured username and password. The password is used in clear
     * form as required by the HTTP Basic authentication specification.
     *
     * @param username the username used for outbound authentication
     * @param password the raw password used for outbound authentication
     * @return a configured RestClient
     */
	@Bean
	RestClient restClient(@Value("${app.auth.username}") String user, @Value("${app.auth.password}") String pass) {
		String token = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes(StandardCharsets.UTF_8));
		return RestClient.builder().defaultHeader("Authorization", "Basic " + token)
				.defaultHeader("Accept", "application/json").build();
	}
}