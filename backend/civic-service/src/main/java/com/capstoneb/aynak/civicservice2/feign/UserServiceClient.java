package com.capstoneb.aynak.civicservice2.feign;

import com.capstoneb.aynak.civicservice2.dto.AdjustPointsRequest;
import com.capstoneb.aynak.civicservice2.dto.CitizenLookupDTO;
import com.capstoneb.aynak.civicservice2.dto.EnsureCitizenRequest;
import com.capstoneb.aynak.civicservice2.dto.PointsBalanceDTO;
import com.capstoneb.aynak.civicservice2.dto.SponsorUserLookupDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("USER-SERVICE2")
public interface UserServiceClient {

	@GetMapping("/internal/citizens/by-email/{email}")
	CitizenLookupDTO findCitizenByEmail(@PathVariable String email);

	@PostMapping("/internal/citizens/ensure")
	CitizenLookupDTO ensureCitizen(@RequestBody EnsureCitizenRequest request);

	@GetMapping("/internal/citizens/{citizenId}/points")
	PointsBalanceDTO getPoints(@PathVariable Integer citizenId);

	@PostMapping("/internal/citizens/{citizenId}/points/adjust")
	PointsBalanceDTO adjustPoints(@PathVariable Integer citizenId, @RequestBody AdjustPointsRequest request);

	@GetMapping("/internal/sponsor-users/by-email/{email}")
	SponsorUserLookupDTO findSponsorUserByEmail(@PathVariable String email);
}
