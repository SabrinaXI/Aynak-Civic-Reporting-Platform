package com.capstoneb.aynak.userservice2.security;

import com.capstoneb.aynak.userservice2.service.AuthorityUserService;
import com.capstoneb.aynak.userservice2.service.CitizenService;
import com.capstoneb.aynak.userservice2.service.SponsorUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

/**
 * Replaces the monolith's CustomOAuth2SuccessHandler, which only worked because Spring Boot
 * was an OAuth2 Client with a login-success event to hook. A resource server has no such event,
 * so JIT provisioning instead runs on every authenticated request, after JWT auth has already
 * populated the SecurityContext with the mapped ROLE_* authorities.
 */
@Component
public class JitProvisioningFilter extends OncePerRequestFilter {

	private final CitizenService citizenService;
	private final AuthorityUserService authorityUserService;
	private final SponsorUserService sponsorUserService;

	public JitProvisioningFilter(CitizenService citizenService, AuthorityUserService authorityUserService,
			SponsorUserService sponsorUserService) {
		this.citizenService = citizenService;
		this.authorityUserService = authorityUserService;
		this.sponsorUserService = sponsorUserService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication instanceof JwtAuthenticationToken jwtAuth) {
			Jwt jwt = jwtAuth.getToken();
			Collection<? extends GrantedAuthority> authorities = jwtAuth.getAuthorities();

			// Every user also carries ROLE_CITIZEN via the realm's default-roles-aynak
			// composite, so the more specific roles must be checked first.
			if (hasRole(authorities, "ROLE_AUTHORITY")) {
				authorityUserService.ensureAuthorityUser(jwt);
			} else if (hasRole(authorities, "ROLE_SPONSOR")) {
				sponsorUserService.ensureSponsorUser(jwt);
			} else if (hasRole(authorities, "ROLE_CITIZEN")) {
				citizenService.ensureCitizen(jwt);
			}
		}

		filterChain.doFilter(request, response);
	}

	private boolean hasRole(Collection<? extends GrantedAuthority> authorities, String role) {
		return authorities.stream().anyMatch(authority -> authority.getAuthority().equals(role));
	}
}
