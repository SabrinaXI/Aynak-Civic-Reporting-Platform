package com.capstoneb.aynak.apigateway2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS now lives here instead of in each downstream service, since the gateway is the single
 * edge the browser talks to.
 */
@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {

		CorsConfiguration config = new CorsConfiguration();

		config.addAllowedOrigin("http://localhost:5173");
		config.addAllowedMethod("GET");
		config.addAllowedMethod("POST");
		config.addAllowedMethod("PUT");
		config.addAllowedMethod("DELETE");
		config.addAllowedMethod("OPTIONS");
		config.addAllowedHeader("*");

		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}
}
