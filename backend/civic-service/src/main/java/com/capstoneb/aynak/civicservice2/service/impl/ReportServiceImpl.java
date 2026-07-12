package com.capstoneb.aynak.civicservice2.service.impl;

import com.capstoneb.aynak.civicservice2.dto.AdjustPointsRequest;
import com.capstoneb.aynak.civicservice2.dto.CitizenContext;
import com.capstoneb.aynak.civicservice2.exception.ReportNotFoundException;
import com.capstoneb.aynak.civicservice2.feign.UserServiceClient;
import com.capstoneb.aynak.civicservice2.model.Report;
import com.capstoneb.aynak.civicservice2.repository.ReportRepo;
import com.capstoneb.aynak.civicservice2.service.NotificationService;
import com.capstoneb.aynak.civicservice2.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

	private static final String POINTS_REASON_REPORT_APPROVED = "REPORT_APPROVED";
	private static final int APPROVAL_POINTS_AWARD = 100;

	private final ReportRepo reportRepo;
	private final NotificationService notificationService;
	private final UserServiceClient userServiceClient;

	public ReportServiceImpl(ReportRepo reportRepo, NotificationService notificationService,
			UserServiceClient userServiceClient) {
		this.reportRepo = reportRepo;
		this.notificationService = notificationService;
		this.userServiceClient = userServiceClient;
	}

	@Override
	public Report createReport(String title, String description, String category, String locationName,
			Double latitude, Double longitude, MultipartFile photo, CitizenContext citizen) throws IOException {

		Report report = new Report();
		report.setTitle(title);
		report.setDescription(description);
		report.setCategory(category);
		report.setLocationName(locationName);
		report.setLatitude(latitude);
		report.setLongitude(longitude);

		report.setCitizenId(citizen.getCitizenId());
		report.setCitizenEmail(citizen.getEmail());
		report.setCreatedAt(LocalDateTime.now());
		report.setStatus("PENDING");

		if (photo != null && !photo.isEmpty()) {
			report.setPhoto(photo.getBytes());
			report.setPhotoContentType(photo.getContentType());
		}

		return reportRepo.save(report);
	}

	@Override
	public List<Report> getMyReports(CitizenContext citizen) {
		return reportRepo.findByCitizenId(citizen.getCitizenId());
	}

	@Override
	public Report getReportById(Integer id) {
		return reportRepo.findById(id)
				.orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + id));
	}

	@Override
	public List<Report> getAllReports() {
		return reportRepo.findAll();
	}

	@Override
	public void deleteMyPendingReport(Integer reportId, CitizenContext citizen) {

		Report report = reportRepo.findById(reportId)
				.orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + reportId));

		if (!report.getCitizenId().equals(citizen.getCitizenId())) {
			throw new RuntimeException("You can only delete your own reports");
		}

		if (!report.getStatus().equals("PENDING")) {
			throw new RuntimeException("Only pending reports can be deleted");
		}

		reportRepo.delete(report);
	}

	@Override
	public Report approveReport(Integer reportId) {

		Report report = reportRepo.findById(reportId)
				.orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + reportId));

		// Award points before persisting APPROVED: if this Feign call fails, the report stays
		// PENDING (a visible, retryable failure) instead of silently approving with no points
		// awarded. adjustPoints is idempotent on (citizenId, reason, referenceId), so retrying
		// this exact approval after a transient failure won't double-award.
		userServiceClient.adjustPoints(report.getCitizenId(),
				new AdjustPointsRequest(APPROVAL_POINTS_AWARD, POINTS_REASON_REPORT_APPROVED, reportId));

		report.setStatus("APPROVED");

		notificationService.notifyCitizen(
				report,
				"Your report titled \"" + report.getTitle() + "\" has been approved!\n\n" +
						"As a thank you, you have received 100 Aynak Points.\n\n" +
						"Thank you for helping improve the UAE community."
		);

		return reportRepo.save(report);
	}

	@Override
	public Report rejectReport(Integer reportId) {
		Report report = reportRepo.findById(reportId)
				.orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + reportId));
		report.setStatus("REJECTED");
		notificationService.notifyCitizen(report,
				"Your report titled \"" + report.getTitle() + "\" was not approved after review.\n\n"
						+ "Thank you for taking the time to submit a report and help improve the UAE community.");
		return reportRepo.save(report);
	}

	@Override
	public List<Report> getReportsByStatus(String status) {
		return reportRepo.findByStatus(status);
	}

	@Override
	public Report updateReportStatus(Integer reportId, String status) {

		Report report = reportRepo.findById(reportId)
				.orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + reportId));
		report.setStatus(status);

		if (status.equals("IN_PROGRESS")) {
			notificationService.notifyCitizen(
					report,
					"Good news! Your report titled \"" + report.getTitle()
							+ "\" is now being addressed by the relevant authority."
			);
		}
		if (status.equals("RESOLVED")) {
			notificationService.notifyCitizen(
					report,
					"Your report titled \"" + report.getTitle() + "\" has been resolved.\n\n" +
							"Thank you for helping make the UAE a better place for everyone."
			);
		}
		return reportRepo.save(report);
	}

	@Override
	public Map<String, Long> getReportsByCategory() {

		Map<String, Long> result = new HashMap<>();

		for (Object[] row : reportRepo.countReportsByCategory()) {
			result.put((String) row[0], (Long) row[1]);
		}

		return result;
	}

	@Override
	public Map<String, Long> getReportsByStatus() {

		Map<String, Long> result = new HashMap<>();

		for (Object[] row : reportRepo.countReportsByStatus()) {
			result.put(row[0].toString(), (Long) row[1]);
		}

		return result;
	}
}
