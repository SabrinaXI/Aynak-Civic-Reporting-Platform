package com.capstoneb.aynak.userservice2.repository;

import com.capstoneb.aynak.userservice2.model.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsorRepo extends JpaRepository<Sponsor, Integer> {

	Sponsor findBySponsorName(String sponsorName);
}
