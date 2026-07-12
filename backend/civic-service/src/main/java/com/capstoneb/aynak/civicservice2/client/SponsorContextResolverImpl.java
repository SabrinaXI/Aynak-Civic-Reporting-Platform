package com.capstoneb.aynak.civicservice2.client;

import com.capstoneb.aynak.civicservice2.dto.SponsorContext;
import com.capstoneb.aynak.civicservice2.dto.SponsorUserLookupDTO;
import com.capstoneb.aynak.civicservice2.feign.UserServiceClient;
import feign.FeignException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

/**
 * Resolves the calling sponsor user's sponsorId via user-service2. Unlike citizens, sponsor
 * users are expected to have already visited their dashboard (and so already exist in
 * user-service2) before ever creating/listing rewards here, so a 404 is a real error rather
 * than a trigger for JIT creation.
 */
@Component
public class SponsorContextResolverImpl implements SponsorContextResolver {

	private final UserServiceClient userServiceClient;

	public SponsorContextResolverImpl(UserServiceClient userServiceClient) {
		this.userServiceClient = userServiceClient;
	}

	@Override
	public SponsorContext resolve(Jwt jwt) {

		String email = jwt.getClaimAsString("email");

		try {
			SponsorUserLookupDTO sponsorUser = userServiceClient.findSponsorUserByEmail(email);
			return new SponsorContext(sponsorUser.getSponsorId(), sponsorUser.getEmail());
		} catch (FeignException.NotFound notFound) {
			throw new RuntimeException(
					"Sponsor profile not found for " + email + " - please visit your sponsor dashboard first");
		}
	}
}
