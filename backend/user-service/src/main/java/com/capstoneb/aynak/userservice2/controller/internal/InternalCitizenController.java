package com.capstoneb.aynak.userservice2.controller.internal;

import com.capstoneb.aynak.userservice2.dto.AdjustPointsRequest;
import com.capstoneb.aynak.userservice2.dto.CitizenLookupDTO;
import com.capstoneb.aynak.userservice2.dto.EnsureCitizenRequest;
import com.capstoneb.aynak.userservice2.dto.PointsBalanceDTO;
import com.capstoneb.aynak.userservice2.model.Citizen;
import com.capstoneb.aynak.userservice2.service.CitizenService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Not routed through api-gateway2 / not internet-facing - called service-to-service
 * (civic-service2 via Feign) over the Eureka-discovered address, so it carries none of the
 * public-facing JWT auth (see SecurityConfig: /internal/** is permitAll).
 */
@RestController
@RequestMapping("/internal/citizens")
public class InternalCitizenController {

	private final CitizenService citizenService;

	public InternalCitizenController(CitizenService citizenService) {
		this.citizenService = citizenService;
	}

	@GetMapping("/by-email/{email}")
	public CitizenLookupDTO byEmail(@PathVariable String email) {
		return toLookupDTO(citizenService.findByEmail(email));
	}

	@PostMapping("/ensure")
	public CitizenLookupDTO ensure(@RequestBody EnsureCitizenRequest request) {
		return toLookupDTO(citizenService.ensureCitizen(request));
	}

	@GetMapping("/{citizenId}/points")
	public PointsBalanceDTO getPoints(@PathVariable Integer citizenId) {
		return new PointsBalanceDTO(citizenId, citizenService.getPoints(citizenId));
	}

	@PostMapping("/{citizenId}/points/adjust")
	public PointsBalanceDTO adjustPoints(@PathVariable Integer citizenId, @RequestBody AdjustPointsRequest request) {
		int newBalance = citizenService.adjustPoints(citizenId, request.getDelta(), request.getReason(),
				request.getReferenceId());
		return new PointsBalanceDTO(citizenId, newBalance);
	}

	private CitizenLookupDTO toLookupDTO(Citizen citizen) {
		return new CitizenLookupDTO(citizen.getId(), citizen.getEmail(), citizen.getFirstName(),
				citizen.getLastName());
	}
}
