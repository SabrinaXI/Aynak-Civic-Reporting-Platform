package com.capstoneb.aynak.userservice2.service;

import com.capstoneb.aynak.userservice2.model.SponsorUser;
import org.springframework.security.oauth2.jwt.Jwt;

public interface SponsorUserService {

	SponsorUser ensureSponsorUser(Jwt jwt);

	SponsorUser findByEmail(String email);
}
