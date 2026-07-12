package com.capstoneb.aynak.civicservice2.service;

import com.capstoneb.aynak.civicservice2.dto.CitizenContext;
import com.capstoneb.aynak.civicservice2.dto.SponsorContext;
import com.capstoneb.aynak.civicservice2.model.RedeemedReward;
import com.capstoneb.aynak.civicservice2.model.Reward;

import java.util.List;

public interface RewardService {

	List<Reward> getActiveRewards();

	RedeemedReward redeemReward(Integer rewardId, CitizenContext citizen);

	List<RedeemedReward> getMyRedeemedRewards(CitizenContext citizen);

	Reward createReward(String brandName, String title, String description, String voucherCode, int pointsRequired,
			SponsorContext sponsor);

	List<Reward> getMySponsorRewards(SponsorContext sponsor);
}
