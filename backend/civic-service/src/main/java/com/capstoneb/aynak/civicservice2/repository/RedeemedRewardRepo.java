package com.capstoneb.aynak.civicservice2.repository;

import com.capstoneb.aynak.civicservice2.model.RedeemedReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RedeemedRewardRepo extends JpaRepository<RedeemedReward, Integer> {

	List<RedeemedReward> findByCitizenId(Integer citizenId);
}
