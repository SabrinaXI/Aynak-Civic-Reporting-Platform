package com.capstoneb.aynak.userservice2.service.impl;

import com.capstoneb.aynak.userservice2.model.Sponsor;
import com.capstoneb.aynak.userservice2.repository.SponsorRepo;
import com.capstoneb.aynak.userservice2.service.SponsorService;
import org.springframework.stereotype.Service;

@Service
public class SponsorServiceImpl implements SponsorService {

	private final SponsorRepo sponsorRepo;

	public SponsorServiceImpl(SponsorRepo sponsorRepo) {
		this.sponsorRepo = sponsorRepo;
	}

	@Override
	public Sponsor findOrCreateSponsor(String sponsorName) {

		Sponsor existingSponsor = sponsorRepo.findBySponsorName(sponsorName);

		if (existingSponsor != null) {
			return existingSponsor;
		}

		Sponsor sponsor = new Sponsor();

		sponsor.setSponsorName(sponsorName);
		sponsor.setContactEmail("");
		sponsor.setDescription(sponsorName + " provides rewards and voucher offers for Aynak citizens.");

		return sponsorRepo.save(sponsor);
	}
}
