package com.capstoneb.aynak.civicservice2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The calling sponsor user's organization, as resolved from the request's JWT. Until Phase 3
 * wires the Feign lookup to user-service2, sponsorId resolves to a fixed placeholder
 * (see SponsorContextResolverImpl).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SponsorContext {

	private Integer sponsorId;
	private String email;
}
