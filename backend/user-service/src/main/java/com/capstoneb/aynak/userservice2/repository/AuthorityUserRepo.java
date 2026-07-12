package com.capstoneb.aynak.userservice2.repository;

import com.capstoneb.aynak.userservice2.model.AuthorityUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityUserRepo extends JpaRepository<AuthorityUser, Integer> {

	AuthorityUser findByEmail(String email);
}
