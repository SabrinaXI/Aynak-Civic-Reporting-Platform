package com.capstoneb.aynak.userservice2.config;

import com.capstoneb.aynak.userservice2.security.JitProvisioningFilter;
import com.capstoneb.aynak.userservice2.security.JwtRoleConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	private final JitProvisioningFilter jitProvisioningFilter;

	public SecurityConfig(JitProvisioningFilter jitProvisioningFilter) {
		this.jitProvisioningFilter = jitProvisioningFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.csrf(csrf -> csrf.disable());

		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/internal/**", "/actuator/**").permitAll()
				.anyRequest().authenticated()
		);

		http.oauth2ResourceServer(oauth2 -> oauth2
				.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
		);

		http.addFilterAfter(jitProvisioningFilter, BearerTokenAuthenticationFilter.class);

		return http.build();
	}

	private JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(new JwtRoleConverter());
		return converter;
	}
}
