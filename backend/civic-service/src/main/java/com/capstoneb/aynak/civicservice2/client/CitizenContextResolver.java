package com.capstoneb.aynak.civicservice2.client;

import com.capstoneb.aynak.civicservice2.dto.CitizenContext;
import org.springframework.security.oauth2.jwt.Jwt;

public interface CitizenContextResolver {

	CitizenContext resolve(Jwt jwt);
}
