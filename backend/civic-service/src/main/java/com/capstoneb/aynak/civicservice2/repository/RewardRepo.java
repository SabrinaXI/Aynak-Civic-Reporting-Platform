package com.capstoneb.aynak.civicservice2.repository;

import com.capstoneb.aynak.civicservice2.model.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardRepo extends JpaRepository<Reward, Integer> {

	List<Reward> findByActiveTrue();

	List<Reward> findBySponsorId(Integer sponsorId);
}
