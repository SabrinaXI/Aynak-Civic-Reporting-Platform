package com.capstoneb.aynak.civicservice2.repository;

import com.capstoneb.aynak.civicservice2.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReportRepo extends JpaRepository<Report, Integer> {

	List<Report> findByCitizenId(Integer citizenId);

	List<Report> findByStatus(String status);

	@Query("""
			SELECT r.category, COUNT(r)
			FROM Report r
			GROUP BY r.category
			""")
	List<Object[]> countReportsByCategory();

	@Query("""
			SELECT r.status, COUNT(r)
			FROM Report r
			GROUP BY r.status
			""")
	List<Object[]> countReportsByStatus();

	// Backs the bulk leaderboard endpoint user-service2 calls instead of looping a per-citizen
	// count over Feign. Preserves the monolith's exact status set (APPROVED/IN_PROGRESS/RESOLVED).
	@Query("""
			SELECT r.citizenId, COUNT(r)
			FROM Report r
			WHERE r.status IN ('APPROVED', 'IN_PROGRESS', 'RESOLVED')
			GROUP BY r.citizenId
			""")
	List<Object[]> countApprovedReportsGroupedByCitizen();
}
