package com.capstoneb.aynak.civicservice2.service.impl;

import com.capstoneb.aynak.civicservice2.dto.AdjustPointsRequest;
import com.capstoneb.aynak.civicservice2.dto.CitizenContext;
import com.capstoneb.aynak.civicservice2.dto.PointsBalanceDTO;
import com.capstoneb.aynak.civicservice2.dto.SponsorContext;
import com.capstoneb.aynak.civicservice2.feign.UserServiceClient;
import com.capstoneb.aynak.civicservice2.model.RedeemedReward;
import com.capstoneb.aynak.civicservice2.model.Reward;
import com.capstoneb.aynak.civicservice2.repository.RedeemedRewardRepo;
import com.capstoneb.aynak.civicservice2.repository.RewardRepo;
import com.capstoneb.aynak.civicservice2.service.RewardService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RewardServiceImpl implements RewardService {

	private static final String POINTS_REASON_REWARD_REDEEMED = "REWARD_REDEEMED";

	private final RewardRepo rewardRepo;
	private final RedeemedRewardRepo redeemedRewardRepo;
	private final UserServiceClient userServiceClient;

	public RewardServiceImpl(RewardRepo rewardRepo, RedeemedRewardRepo redeemedRewardRepo,
			UserServiceClient userServiceClient) {
		this.rewardRepo = rewardRepo;
		this.redeemedRewardRepo = redeemedRewardRepo;
		this.userServiceClient = userServiceClient;
	}

	@Override
	public List<Reward> getActiveRewards() {
		return rewardRepo.findByActiveTrue();
	}

	@Override
	public RedeemedReward redeemReward(Integer rewardId, CitizenContext citizen) {

		Reward reward = rewardRepo.findById(rewardId)
				.orElseThrow(() -> new RuntimeException("Reward not found"));

		if (!reward.isActive()) {
			throw new RuntimeException("Reward is not active");
		}

		PointsBalanceDTO balance = userServiceClient.getPoints(citizen.getCitizenId());

		if (balance.getPoints() < reward.getPointsRequired()) {
			throw new RuntimeException("Not enough Aynak points");
		}

		// Deduct before persisting RedeemedReward, not after: if this Feign call fails, the
		// redemption simply doesn't go through, instead of the citizen keeping both their points
		// and a validated voucher.
		userServiceClient.adjustPoints(citizen.getCitizenId(),
				new AdjustPointsRequest(-reward.getPointsRequired(), POINTS_REASON_REWARD_REDEEMED, rewardId));

		RedeemedReward redeemedReward = new RedeemedReward();

		redeemedReward.setCitizenId(citizen.getCitizenId());
		redeemedReward.setReward(reward);
		redeemedReward.setVoucherCode(reward.getVoucherCode());
		redeemedReward.setRedeemedAt(LocalDateTime.now());

		return redeemedRewardRepo.save(redeemedReward);
	}

	@Override
	public List<RedeemedReward> getMyRedeemedRewards(CitizenContext citizen) {
		return redeemedRewardRepo.findByCitizenId(citizen.getCitizenId());
	}

	@Override
	public Reward createReward(String brandName, String title, String description, String voucherCode,
			int pointsRequired, SponsorContext sponsor) {

		Reward reward = new Reward();

		reward.setBrandName(brandName);
		reward.setTitle(title);
		reward.setDescription(description);
		reward.setVoucherCode(voucherCode);
		reward.setPointsRequired(pointsRequired);
		reward.setActive(true);
		reward.setSponsorId(sponsor.getSponsorId());

		return rewardRepo.save(reward);
	}

	@Override
	public List<Reward> getMySponsorRewards(SponsorContext sponsor) {
		return rewardRepo.findBySponsorId(sponsor.getSponsorId());
	}
}
