package com.capstoneb.aynak.civicservice2.controller;

import com.capstoneb.aynak.civicservice2.client.CitizenContextResolver;
import com.capstoneb.aynak.civicservice2.client.SponsorContextResolver;
import com.capstoneb.aynak.civicservice2.dto.CitizenContext;
import com.capstoneb.aynak.civicservice2.dto.SponsorContext;
import com.capstoneb.aynak.civicservice2.model.RedeemedReward;
import com.capstoneb.aynak.civicservice2.model.Reward;
import com.capstoneb.aynak.civicservice2.service.RewardService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

	private final RewardService rewardService;
	private final CitizenContextResolver citizenContextResolver;
	private final SponsorContextResolver sponsorContextResolver;

	public RewardController(RewardService rewardService, CitizenContextResolver citizenContextResolver,
			SponsorContextResolver sponsorContextResolver) {
		this.rewardService = rewardService;
		this.citizenContextResolver = citizenContextResolver;
		this.sponsorContextResolver = sponsorContextResolver;
	}

	@GetMapping
	public List<Reward> getRewards() {
		return rewardService.getActiveRewards();
	}

	@PostMapping("/{id}/redeem")
	public ResponseEntity<RedeemedReward> redeemReward(@PathVariable Integer id, Authentication authentication) {
		CitizenContext citizen = citizenContextResolver.resolve((Jwt) authentication.getPrincipal());
		RedeemedReward redeemedReward = rewardService.redeemReward(id, citizen);
		return ResponseEntity.ok(redeemedReward);
	}

	@GetMapping("/my-redeemed")
	public List<RedeemedReward> getMyRedeemedRewards(Authentication authentication) {
		CitizenContext citizen = citizenContextResolver.resolve((Jwt) authentication.getPrincipal());
		return rewardService.getMyRedeemedRewards(citizen);
	}

	@PostMapping("/sponsor/create")
	public ResponseEntity<Reward> createReward(
			@RequestParam String brandName,
			@RequestParam String title,
			@RequestParam String description,
			@RequestParam String voucherCode,
			@RequestParam int pointsRequired,
			Authentication authentication
	) {
		SponsorContext sponsor = sponsorContextResolver.resolve((Jwt) authentication.getPrincipal());
		Reward reward = rewardService.createReward(brandName, title, description, voucherCode, pointsRequired, sponsor);
		return ResponseEntity.ok(reward);
	}

	@GetMapping("/sponsor/my")
	public List<Reward> getMySponsorRewards(Authentication authentication) {
		SponsorContext sponsor = sponsorContextResolver.resolve((Jwt) authentication.getPrincipal());
		return rewardService.getMySponsorRewards(sponsor);
	}
}
