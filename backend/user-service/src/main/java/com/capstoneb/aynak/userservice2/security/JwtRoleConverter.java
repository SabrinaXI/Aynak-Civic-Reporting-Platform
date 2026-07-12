package com.capstoneb.aynak.userservice2.security;

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
 * Mirrors the role-mapping logic from the monolith's SecurityConfig.oidcUserService(),
 * just reading the realm_access.roles claim straight off the Jwt instead of an OidcUser.
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
