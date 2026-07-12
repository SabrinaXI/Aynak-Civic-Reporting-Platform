package com.capstoneb.aynak.civicservice2.service;

import com.capstoneb.aynak.civicservice2.dto.CitizenContext;
import com.capstoneb.aynak.civicservice2.model.Report;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ReportService {

	Report createReport(String title, String description, String category, String locationName, Double latitude,
			Double longitude, MultipartFile photo, CitizenContext citizen) throws IOException;

	List<Report> getMyReports(CitizenContext citizen);

	Report getReportById(Integer id);

	List<Report> getAllReports();

	void deleteMyPendingReport(Integer reportId, CitizenContext citizen);

	Report approveReport(Integer reportId);

	Report rejectReport(Integer reportId);

	List<Report> getReportsByStatus(String status);

	Report updateReportStatus(Integer reportId, String status);

	Map<String, Long> getReportsByCategory();

	Map<String, Long> getReportsByStatus();
}
