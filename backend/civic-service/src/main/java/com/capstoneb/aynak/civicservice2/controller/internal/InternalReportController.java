package com.capstoneb.aynak.civicservice2.controller.internal;

import com.capstoneb.aynak.civicservice2.dto.ReportCountDTO;
import com.capstoneb.aynak.civicservice2.repository.ReportRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Not routed through api-gateway2 - called service-to-service (user-service2 via Feign) for the
 * leaderboard, as a single bulk query instead of an N+1 per-citizen loop.
 */
@RestController
@RequestMapping("/internal/reports")
public class InternalReportController {

	private final ReportRepo reportRepo;

	public InternalReportController(ReportRepo reportRepo) {
		this.reportRepo = reportRepo;
	}

	@GetMapping("/approved-counts")
	public List<ReportCountDTO> getApprovedCounts() {

		List<ReportCountDTO> counts = new ArrayList<>();

		for (Object[] row : reportRepo.countApprovedReportsGroupedByCitizen()) {
			counts.add(new ReportCountDTO((Integer) row[0], ((Long) row[1]).intValue()));
		}

		return counts;
	}
}
