package com.capstoneb.aynak.userservice2.service;

import com.capstoneb.aynak.userservice2.dto.EnsureCitizenRequest;
import com.capstoneb.aynak.userservice2.dto.LeaderboardUserDTO;
import com.capstoneb.aynak.userservice2.model.Citizen;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface CitizenService {

	Citizen ensureCitizen(Jwt jwt);

	Citizen ensureCitizen(EnsureCitizenRequest request);

	Citizen findByEmail(String email);

	int getPoints(Integer citizenId);

	int adjustPoints(Integer citizenId, int delta, String reason, Integer referenceId);

	List<LeaderboardUserDTO> getLeaderboard();

	LeaderboardUserDTO getMyRank(String email);
}
