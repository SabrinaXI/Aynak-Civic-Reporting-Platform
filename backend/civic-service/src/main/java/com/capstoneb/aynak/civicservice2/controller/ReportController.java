package com.capstoneb.aynak.civicservice2.controller;

import com.capstoneb.aynak.civicservice2.client.CitizenContextResolver;
import com.capstoneb.aynak.civicservice2.dto.CitizenContext;
import com.capstoneb.aynak.civicservice2.model.Report;
import com.capstoneb.aynak.civicservice2.service.ReportService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	private final ReportService reportService;
	private final CitizenContextResolver citizenContextResolver;

	public ReportController(ReportService reportService, CitizenContextResolver citizenContextResolver) {
		this.reportService = reportService;
		this.citizenContextResolver = citizenContextResolver;
	}

	@PostMapping
	public ResponseEntity<String> submitReport(@RequestParam String title, @RequestParam String description,
			@RequestParam String category, @RequestParam String locationName, @RequestParam Double latitude,
			@RequestParam Double longitude, @RequestParam MultipartFile photo, Authentication authentication)
			throws IOException {
		reportService.createReport(title, description, category, locationName, latitude, longitude, photo,
				citizenContext(authentication));
		return ResponseEntity.ok("Report submitted successfully");
	}

	@GetMapping
	public List<Report> getAllReports() {
		return reportService.getAllReports();
	}

	@GetMapping("/photo/{id}")
	public ResponseEntity<byte[]> getReportPhoto(@PathVariable Integer id) {

		Report report = reportService.getReportById(id);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(report.getPhotoContentType()))
				.body(report.getPhoto());
	}

	@GetMapping("/my")
	public List<Report> getMyReports(Authentication authentication) {
		return reportService.getMyReports(citizenContext(authentication));
	}

	@DeleteMapping("/my/{id}")
	public ResponseEntity<String> deleteMyPendingReport(@PathVariable Integer id, Authentication authentication) {
		reportService.deleteMyPendingReport(id, citizenContext(authentication));
		return ResponseEntity.ok("Report deleted successfully");
	}

	@PutMapping("/authority/{id}/approve")
	public ResponseEntity<String> approveReport(@PathVariable Integer id) {
		reportService.approveReport(id);
		return ResponseEntity.ok("Report approved successfully");
	}

	@PutMapping("/authority/{id}/reject")
	public ResponseEntity<String> rejectReport(@PathVariable Integer id) {
		reportService.rejectReport(id);
		return ResponseEntity.ok("Report rejected successfully");
	}

	@GetMapping("/authority/pending")
	public List<Report> getPendingReports() {
		return reportService.getReportsByStatus("PENDING");
	}

	@GetMapping("/authority/approved")
	public List<Report> getApprovedReports() {
		return reportService.getReportsByStatus("APPROVED");
	}

	@PutMapping("/authority/{id}/in-progress")
	public ResponseEntity<String> markInProgress(@PathVariable Integer id) {
		reportService.updateReportStatus(id, "IN_PROGRESS");
		return ResponseEntity.ok("Report marked as in progress");
	}

	@PutMapping("/authority/{id}/resolved")
	public ResponseEntity<String> markResolved(@PathVariable Integer id) {
		reportService.updateReportStatus(id, "RESOLVED");
		return ResponseEntity.ok("Report marked as resolved");
	}

	@GetMapping("/analytics/category")
	public Map<String, Long> reportsByCategory() {
		return reportService.getReportsByCategory();
	}

	@GetMapping("/analytics/status")
	public Map<String, Long> reportsByStatus() {
		return reportService.getReportsByStatus();
	}

	private CitizenContext citizenContext(Authentication authentication) {
		return citizenContextResolver.resolve((Jwt) authentication.getPrincipal());
	}
}
