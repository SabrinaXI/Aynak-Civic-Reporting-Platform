package com.capstoneb.aynak.userservice2.service.impl;

import com.capstoneb.aynak.userservice2.model.Authority;
import com.capstoneb.aynak.userservice2.repository.AuthorityRepo;
import com.capstoneb.aynak.userservice2.service.AuthorityService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthorityServiceImpl implements AuthorityService {

	private final AuthorityRepo authorityRepo;

	public AuthorityServiceImpl(AuthorityRepo authorityRepo) {
		this.authorityRepo = authorityRepo;
	}

	@Override
	public Authority findOrCreateAuthority(String authorityName) {

		Authority existingAuthority = authorityRepo.findByAuthorityName(authorityName);

		if (existingAuthority != null) {
			return existingAuthority;
		}

		Authority authority = new Authority();

		authority.setAuthorityName(authorityName);
		authority.setCreatedAt(LocalDateTime.now());

		if (authorityName.equalsIgnoreCase("Abu Dhabi Municipality")) {
			authority.setAuthorityCategory("Municipality");
			authority.setContactEmail("info@dmt.gov.ae");
			authority.setDescription(
					"Abu Dhabi Municipality is responsible for reviewing and managing civic reports related to public spaces, infrastructure, roads, cleanliness, and community services.");

		} else if (authorityName.equalsIgnoreCase("Dubai Municipality")) {
			authority.setAuthorityCategory("Municipality");
			authority.setContactEmail("info@dm.gov.ae");
			authority.setDescription(
					"Dubai Municipality is responsible for reviewing and managing civic reports related to municipal services, public facilities, cleanliness, and infrastructure.");

		} else if (authorityName.equalsIgnoreCase("Abu Dhabi Police")) {
			authority.setAuthorityCategory("Police");
			authority.setContactEmail("");
			authority.setDescription(
					"Abu Dhabi Police is responsible for reviewing and managing reports related to public safety and security incidents.");

		} else if (authorityName.equalsIgnoreCase("Environment Agency Abu Dhabi")) {
			authority.setAuthorityCategory("Environmental Authority");
			authority.setContactEmail("");
			authority.setDescription(
					"Environment Agency Abu Dhabi is responsible for reviewing and managing environmental reports and sustainability-related incidents.");

		} else if (authorityName.equalsIgnoreCase("Department of Transport")) {
			authority.setAuthorityCategory("Transport Authority");
			authority.setContactEmail("");
			authority.setDescription(
					"Department of Transport is responsible for reviewing and managing reports related to transportation, roads, and mobility services.");

		} else {
			authority.setAuthorityCategory("Government Authority");
			authority.setContactEmail("");
			authority.setDescription(
					authorityName + " is responsible for reviewing and managing assigned civic reports.");
		}

		return authorityRepo.save(authority);
	}
}
