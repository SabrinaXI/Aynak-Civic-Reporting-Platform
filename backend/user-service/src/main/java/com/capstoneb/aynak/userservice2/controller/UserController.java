package com.capstoneb.aynak.userservice2.controller;

import com.capstoneb.aynak.userservice2.dto.LeaderboardUserDTO;
import com.capstoneb.aynak.userservice2.service.AuthorityUserService;
import com.capstoneb.aynak.userservice2.service.CitizenService;
import com.capstoneb.aynak.userservice2.service.SponsorUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final CitizenService citizenService;
	private final AuthorityUserService authorityUserService;
	private final SponsorUserService sponsorUserService;

	public UserController(CitizenService citizenService, AuthorityUserService authorityUserService,
			SponsorUserService sponsorUserService) {
		this.citizenService = citizenService;
		this.authorityUserService = authorityUserService;
		this.sponsorUserService = sponsorUserService;
	}

	@GetMapping("/me")
	public Object getCurrentUser(Authentication authentication) {

		Jwt jwt = (Jwt) authentication.getPrincipal();
		String email = jwt.getClaimAsString("email");

		boolean isAuthority = false;
		boolean isSponsor = false;
		boolean isCitizen = false;

		for (GrantedAuthority authority : authentication.getAuthorities()) {
			String role = authority.getAuthority();

			if (role.equals("ROLE_AUTHORITY")) {
				isAuthority = true;
			}
			if (role.equals("ROLE_SPONSOR")) {
				isSponsor = true;
			}
			if (role.equals("ROLE_CITIZEN")) {
				isCitizen = true;
			}
		}

		if (isAuthority) {
			return authorityUserService.findByEmail(email);
		}

		if (isSponsor) {
			return sponsorUserService.findByEmail(email);
		}

		if (isCitizen) {
			return citizenService.findByEmail(email);
		}

		throw new RuntimeException("User role not supported");
	}

	@GetMapping("/leaderboard")
	public List<LeaderboardUserDTO> getLeaderboard() {
		return citizenService.getLeaderboard();
	}

	@GetMapping("/me/rank")
	public LeaderboardUserDTO getMyRank(Authentication authentication) {
		Jwt jwt = (Jwt) authentication.getPrincipal();
		return citizenService.getMyRank(jwt.getClaimAsString("email"));
	}
}
