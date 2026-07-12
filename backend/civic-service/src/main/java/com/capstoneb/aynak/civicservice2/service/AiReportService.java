package com.capstoneb.aynak.civicservice2.service;

import com.capstoneb.aynak.civicservice2.dto.AiReportAnalysisDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AiReportService {

	AiReportAnalysisDTO analyzeReportImage(MultipartFile photo) throws IOException;
}
