package com.capstoneb.aynak.userservice2.service.impl;

import com.capstoneb.aynak.userservice2.model.Authority;
import com.capstoneb.aynak.userservice2.model.AuthorityUser;
import com.capstoneb.aynak.userservice2.repository.AuthorityUserRepo;
import com.capstoneb.aynak.userservice2.service.AuthorityService;
import com.capstoneb.aynak.userservice2.service.AuthorityUserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuthorityUserServiceImpl implements AuthorityUserService {

	private final AuthorityUserRepo authorityUserRepo;
	private final AuthorityService authorityService;

	public AuthorityUserServiceImpl(AuthorityUserRepo authorityUserRepo, AuthorityService authorityService) {
		this.authorityUserRepo = authorityUserRepo;
		this.authorityService = authorityService;
	}

	@Override
	public AuthorityUser ensureAuthorityUser(Jwt jwt) {

		String email = jwt.getClaimAsString("email");

		AuthorityUser existingUser = authorityUserRepo.findByEmail(email);

		if (existingUser != null) {
			return existingUser;
		}

		String authorityName = "Abu Dhabi Municipality";

		List<String> groups = jwt.getClaimAsStringList("groups");

		if (groups != null && !groups.isEmpty()) {
			authorityName = groups.get(0).replace("/", "").trim();
		}

		Authority authority = authorityService.findOrCreateAuthority(authorityName);

		AuthorityUser authorityUser = new AuthorityUser();

		authorityUser.setEmail(email);
		authorityUser.setFirstName(jwt.getClaimAsString("given_name"));
		authorityUser.setLastName(jwt.getClaimAsString("family_name"));
		authorityUser.setRole("AUTHORITY");
		authorityUser.setJobTitle("Authority Officer");
		authorityUser.setCreatedAt(LocalDateTime.now());
		authorityUser.setAuthority(authority);

		try {
			return authorityUserRepo.save(authorityUser);
		} catch (DataIntegrityViolationException e) {
			AuthorityUser createdConcurrently = authorityUserRepo.findByEmail(email);
			if (createdConcurrently != null) {
				return createdConcurrently;
			}
			throw e;
		}
	}

	@Override
	public AuthorityUser findByEmail(String email) {
		return authorityUserRepo.findByEmail(email);
	}
}
