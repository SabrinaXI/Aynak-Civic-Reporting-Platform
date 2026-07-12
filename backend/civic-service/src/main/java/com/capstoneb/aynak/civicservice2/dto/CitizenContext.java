package com.capstoneb.aynak.civicservice2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The calling citizen, as resolved from the request's JWT. Until Phase 3 wires the Feign
 * lookup to user-service2, citizenId resolves to a fixed placeholder (see CitizenContextResolverImpl).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenContext {

	private Integer citizenId;
	private String email;
}
