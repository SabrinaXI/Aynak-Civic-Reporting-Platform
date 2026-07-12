package com.capstoneb.aynak.userservice2.service;

import com.capstoneb.aynak.userservice2.model.Sponsor;

public interface SponsorService {

	Sponsor findOrCreateSponsor(String sponsorName);
}
