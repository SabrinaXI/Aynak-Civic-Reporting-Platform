package com.capstoneb.aynak.civicservice2.client;

import com.capstoneb.aynak.civicservice2.dto.SponsorContext;
import org.springframework.security.oauth2.jwt.Jwt;

public interface SponsorContextResolver {

	SponsorContext resolve(Jwt jwt);
}
