package com.capstoneb.aynak.civicservice2.client;

import com.capstoneb.aynak.civicservice2.dto.CitizenLookupDTO;
import com.capstoneb.aynak.civicservice2.dto.CitizenContext;
import com.capstoneb.aynak.civicservice2.dto.EnsureCitizenRequest;
import com.capstoneb.aynak.civicservice2.feign.UserServiceClient;
import feign.FeignException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Resolves the calling citizen's user-service2 citizenId by email. If this citizen has never
 * authenticated against user-service2 before (their first action might be submitting a report
 * here), a 404 triggers the same idempotent get-or-create logic via /internal/citizens/ensure
 * rather than assuming the JIT-provisioning filter already ran for them.
 */
@Component
public class CitizenContextResolverImpl implements CitizenContextResolver {

	private final UserServiceClient userServiceClient;

	public CitizenContextResolverImpl(UserServiceClient userServiceClient) {
		this.userServiceClient = userServiceClient;
	}

	@Override
	public CitizenContext resolve(Jwt jwt) {

		String email = jwt.getClaimAsString("email");

		CitizenLookupDTO citizen;

		try {
			citizen = userServiceClient.findCitizenByEmail(email);
		} catch (FeignException.NotFound notFound) {
			citizen = userServiceClient.ensureCitizen(new EnsureCitizenRequest(
					email,
					jwt.getClaimAsString("given_name"),
					jwt.getClaimAsString("family_name"),
					jwt.getClaimAsString("phone_number"),
					jwt.getClaimAsString("emirate"),
					jwt.getClaimAsString("preferred_language"),
					jwt.getClaimAsString("date_of_birth")
			));
		}

		return new CitizenContext(citizen.getCitizenId(), citizen.getEmail());
	}
}
