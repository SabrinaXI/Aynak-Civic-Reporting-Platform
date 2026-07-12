package com.capstoneb.aynak.civicservice2.controller;

import com.capstoneb.aynak.civicservice2.dto.AiReportAnalysisDTO;
import com.capstoneb.aynak.civicservice2.service.AiReportService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/ai-reports")
public class AiReportController {

	private final AiReportService aiReportService;

	public AiReportController(AiReportService aiReportService) {
		this.aiReportService = aiReportService;
	}

	@PostMapping("/analyze")
	public ResponseEntity<AiReportAnalysisDTO> analyzeImage(@RequestParam MultipartFile photo) throws IOException {
		AiReportAnalysisDTO result = aiReportService.analyzeReportImage(photo);
		return ResponseEntity.ok(result);
	}
}
