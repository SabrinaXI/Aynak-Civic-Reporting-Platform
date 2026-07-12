package com.capstoneb.aynak.civicservice2.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Same role-mapping logic as user-service2's JwtRoleConverter, duplicated rather than shared -
 * this project intentionally has no shared library between services.
 */
public class JwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

	@Override
	@SuppressWarnings("unchecked")
	public Collection<GrantedAuthority> convert(Jwt jwt) {
		Map<String, Object> realmAccess = jwt.getClaim("realm_access");
		Set<GrantedAuthority> authorities = new HashSet<>();

		if (realmAccess != null) {
			List<String> roles = (List<String>) realmAccess.get("roles");
			if (roles != null) {
				for (String role : roles) {
					authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
				}
			}
		}

		return authorities;
	}
}
