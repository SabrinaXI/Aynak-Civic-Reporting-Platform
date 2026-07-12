package com.capstoneb.aynak.userservice2.repository;

import com.capstoneb.aynak.userservice2.model.SponsorUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SponsorUserRepo extends JpaRepository<SponsorUser, Integer> {

	SponsorUser findByEmail(String email);
}
