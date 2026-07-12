package com.capstoneb.aynak.userservice2.service.impl;

import com.capstoneb.aynak.userservice2.model.Sponsor;
import com.capstoneb.aynak.userservice2.model.SponsorUser;
import com.capstoneb.aynak.userservice2.repository.SponsorUserRepo;
import com.capstoneb.aynak.userservice2.service.SponsorService;
import com.capstoneb.aynak.userservice2.service.SponsorUserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SponsorUserServiceImpl implements SponsorUserService {

	private final SponsorUserRepo sponsorUserRepo;
	private final SponsorService sponsorService;

	public SponsorUserServiceImpl(SponsorUserRepo sponsorUserRepo, SponsorService sponsorService) {
		this.sponsorUserRepo = sponsorUserRepo;
		this.sponsorService = sponsorService;
	}

	@Override
	public SponsorUser ensureSponsorUser(Jwt jwt) {

		String email = jwt.getClaimAsString("email");

		SponsorUser existingUser = sponsorUserRepo.findByEmail(email);

		if (existingUser != null) {
			return existingUser;
		}

		String sponsorName = "Carrefour";

		List<String> groups = jwt.getClaimAsStringList("groups");

		if (groups != null && !groups.isEmpty()) {
			sponsorName = groups.get(0).replace("/", "").trim();
		}

		Sponsor sponsor = sponsorService.findOrCreateSponsor(sponsorName);

		SponsorUser sponsorUser = new SponsorUser();

		sponsorUser.setEmail(email);
		sponsorUser.setFirstName(jwt.getClaimAsString("given_name"));
		sponsorUser.setLastName(jwt.getClaimAsString("family_name"));
		sponsorUser.setRole("SPONSOR");
		sponsorUser.setJobTitle("Sponsor Representative");
		sponsorUser.setCreatedAt(LocalDateTime.now());
		sponsorUser.setSponsor(sponsor);

		try {
			return sponsorUserRepo.save(sponsorUser);
		} catch (DataIntegrityViolationException e) {
			SponsorUser createdConcurrently = sponsorUserRepo.findByEmail(email);
			if (createdConcurrently != null) {
				return createdConcurrently;
			}
			throw e;
		}
	}

	@Override
	public SponsorUser findByEmail(String email) {
		return sponsorUserRepo.findByEmail(email);
	}
}
