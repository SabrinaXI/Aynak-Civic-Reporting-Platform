package com.capstoneb.aynak.userservice2.controller.internal;

import com.capstoneb.aynak.userservice2.dto.SponsorUserLookupDTO;
import com.capstoneb.aynak.userservice2.model.SponsorUser;
import com.capstoneb.aynak.userservice2.service.SponsorUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Used by civic-service2 to resolve a sponsor user's sponsorId before creating/listing rewards.
 * Unlike citizens, sponsor users are expected to have already visited user-service2 (e.g. their
 * dashboard) at least once before creating a reward, so there's no "ensure" fallback here.
 */
@RestController
@RequestMapping("/internal/sponsor-users")
public class InternalSponsorUserController {

	private final SponsorUserService sponsorUserService;

	public InternalSponsorUserController(SponsorUserService sponsorUserService) {
		this.sponsorUserService = sponsorUserService;
	}

	@GetMapping("/by-email/{email}")
	public ResponseEntity<SponsorUserLookupDTO> byEmail(@PathVariable String email) {

		SponsorUser sponsorUser = sponsorUserService.findByEmail(email);

		if (sponsorUser == null) {
			return ResponseEntity.notFound().build();
		}

		SponsorUserLookupDTO dto = new SponsorUserLookupDTO(
				sponsorUser.getId(),
				sponsorUser.getEmail(),
				sponsorUser.getSponsor().getId(),
				sponsorUser.getFirstName(),
				sponsorUser.getLastName()
		);

		return ResponseEntity.ok(dto);
	}
}
