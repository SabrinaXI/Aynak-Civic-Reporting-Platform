package com.capstoneb.aynak.userservice2.repository;

import com.capstoneb.aynak.userservice2.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepo extends JpaRepository<Authority, Integer> {

	Authority findByAuthorityName(String authorityName);
}
