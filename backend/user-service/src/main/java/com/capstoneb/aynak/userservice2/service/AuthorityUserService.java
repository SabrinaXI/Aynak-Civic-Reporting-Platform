package com.capstoneb.aynak.userservice2.service;

import com.capstoneb.aynak.userservice2.model.AuthorityUser;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthorityUserService {

	AuthorityUser ensureAuthorityUser(Jwt jwt);

	AuthorityUser findByEmail(String email);
}
