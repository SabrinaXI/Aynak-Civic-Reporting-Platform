package com.capstoneb.aynak.civicservice2.config;

import com.capstoneb.aynak.civicservice2.security.JwtRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable());

		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/internal/**", "/actuator/**").permitAll()

				.requestMatchers("/api/reports/authority/**").hasAnyRole("ADMIN", "AUTHORITY")
				.requestMatchers("/api/reports/admin/**").hasRole("ADMIN")
				.requestMatchers("/api/reports/sponsor/**").hasRole("SPONSOR")
				.requestMatchers("/api/rewards/sponsor/**").hasRole("SPONSOR")
				.requestMatchers("/api/rewards/**").hasRole("CITIZEN")
				.requestMatchers("/api/ai-reports/**").hasRole("CITIZEN")
				.requestMatchers(HttpMethod.GET, "/api/reports").hasAnyRole("CITIZEN", "ADMIN", "AUTHORITY")
				.requestMatchers(HttpMethod.GET, "/api/reports/photo/**").hasAnyRole("CITIZEN", "ADMIN", "AUTHORITY")
				.requestMatchers("/api/reports/my/**").hasRole("CITIZEN")
				.requestMatchers(HttpMethod.POST, "/api/reports").hasRole("CITIZEN")

				.anyRequest().authenticated()
		);

		http.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
		);

		return http.build();
	}

	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(new JwtRoleConverter());
		return converter;
	}
}
