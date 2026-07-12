package com.capstoneb.aynak.userservice2.service.impl;

import com.capstoneb.aynak.userservice2.dto.EnsureCitizenRequest;
import com.capstoneb.aynak.userservice2.dto.LeaderboardUserDTO;
import com.capstoneb.aynak.userservice2.dto.ReportCountDTO;
import com.capstoneb.aynak.userservice2.exception.CitizenNotFoundException;
import com.capstoneb.aynak.userservice2.feign.CivicServiceClient;
import com.capstoneb.aynak.userservice2.model.Citizen;
import com.capstoneb.aynak.userservice2.model.PointsLedger;
import com.capstoneb.aynak.userservice2.repository.CitizenRepo;
import com.capstoneb.aynak.userservice2.repository.PointsLedgerRepo;
import com.capstoneb.aynak.userservice2.service.CitizenService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CitizenServiceImpl implements CitizenService {

	private final CitizenRepo citizenRepo;
	private final PointsLedgerRepo pointsLedgerRepo;
	private final CivicServiceClient civicServiceClient;

	public CitizenServiceImpl(CitizenRepo citizenRepo, PointsLedgerRepo pointsLedgerRepo,
			CivicServiceClient civicServiceClient) {
		this.citizenRepo = citizenRepo;
		this.pointsLedgerRepo = pointsLedgerRepo;
		this.civicServiceClient = civicServiceClient;
	}

	@Override
	public Citizen ensureCitizen(Jwt jwt) {
		return ensureCitizen(new EnsureCitizenRequest(
				jwt.getClaimAsString("email"),
				jwt.getClaimAsString("given_name"),
				jwt.getClaimAsString("family_name"),
				jwt.getClaimAsString("phone_number"),
				jwt.getClaimAsString("emirate"),
				jwt.getClaimAsString("preferred_language"),
				jwt.getClaimAsString("date_of_birth")
		));
	}

	@Override
	public Citizen ensureCitizen(EnsureCitizenRequest request) {

		Citizen existing = citizenRepo.findByEmail(request.getEmail());

		if (existing != null) {
			return existing;
		}

		Citizen citizen = new Citizen();

		citizen.setEmail(request.getEmail());
		citizen.setFirstName(request.getFirstName());
		citizen.setLastName(request.getLastName());
		citizen.setPhoneNumber(request.getPhoneNumber());
		citizen.setEmirate(request.getEmirate());
		citizen.setPreferredLanguage(request.getPreferredLanguage());
		citizen.setRole("CITIZEN");
		citizen.setAynakPoints(0);
		citizen.setCreatedAt(LocalDateTime.now());

		if (request.getDateOfBirth() != null) {
			citizen.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
		}

		try {
			return citizenRepo.save(citizen);
		} catch (DataIntegrityViolationException e) {
			Citizen createdConcurrently = citizenRepo.findByEmail(request.getEmail());
			if (createdConcurrently != null) {
				return createdConcurrently;
			}
			throw e;
		}
	}

	@Override
	public Citizen findByEmail(String email) {

		Citizen citizen = citizenRepo.findByEmail(email);

		if (citizen == null) {
			throw new CitizenNotFoundException("Citizen not found with email: " + email);
		}

		return citizen;
	}

	@Override
	public int getPoints(Integer citizenId) {
		return findById(citizenId).getAynakPoints();
	}

	@Override
	@Transactional
	public int adjustPoints(Integer citizenId, int delta, String reason, Integer referenceId) {

		Citizen citizen = findById(citizenId);

		boolean alreadyApplied = pointsLedgerRepo
				.findByCitizenIdAndReasonAndReferenceId(citizenId, reason, referenceId)
				.isPresent();

		if (alreadyApplied) {
			return citizen.getAynakPoints();
		}

		citizen.setAynakPoints(citizen.getAynakPoints() + delta);

		try {
			pointsLedgerRepo.save(new PointsLedger(null, citizenId, delta, reason, referenceId, LocalDateTime.now()));
		} catch (DataIntegrityViolationException e) {
			// a concurrent retry already recorded this adjustment; treat as already-applied.
			return findById(citizenId).getAynakPoints();
		}

		return citizenRepo.save(citizen).getAynakPoints();
	}

	private Citizen findById(Integer citizenId) {
		return citizenRepo.findById(citizenId)
				.orElseThrow(() -> new CitizenNotFoundException("Citizen not found with id: " + citizenId));
	}

	@Override
	public List<LeaderboardUserDTO> getLeaderboard() {

		Map<Integer, Integer> approvedCountsByCitizenId = new HashMap<>();
		for (ReportCountDTO count : civicServiceClient.getApprovedCounts()) {
			approvedCountsByCitizenId.put(count.getCitizenId(), count.getApprovedCount());
		}

		List<Citizen> citizens = citizenRepo.findAll();
		List<LeaderboardUserDTO> leaderboard = new ArrayList<>();

		for (Citizen citizen : citizens) {

			int approvedReports = approvedCountsByCitizenId.getOrDefault(citizen.getId(), 0);
			String fullName = citizen.getFirstName() + " " + citizen.getLastName();

			leaderboard.add(new LeaderboardUserDTO(citizen.getId(), fullName, approvedReports, 0));
		}

		leaderboard.sort(Comparator.comparingInt(LeaderboardUserDTO::getApprovedReports).reversed());

		for (int i = 0; i < leaderboard.size(); i++) {
			leaderboard.get(i).setRank(i + 1);
		}

		return leaderboard;
	}

	@Override
	public LeaderboardUserDTO getMyRank(String email) {

		Citizen citizen = findByEmail(email);

		List<LeaderboardUserDTO> leaderboard = getLeaderboard();

		for (LeaderboardUserDTO user : leaderboard) {
			if (user.getUserId().equals(citizen.getId())) {
				return user;
			}
		}

		return new LeaderboardUserDTO(citizen.getId(), citizen.getFirstName() + " " + citizen.getLastName(), 0, 0);
	}
}
