package com.capstoneb.aynak.userservice2.repository;

import com.capstoneb.aynak.userservice2.model.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitizenRepo extends JpaRepository<Citizen, Integer> {

	Citizen findByEmail(String email);
}
