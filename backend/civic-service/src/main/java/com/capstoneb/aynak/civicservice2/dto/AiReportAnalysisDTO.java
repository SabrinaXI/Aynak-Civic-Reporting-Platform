package com.capstoneb.aynak.civicservice2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AiReportAnalysisDTO {

	private String title;
	private String description;
	private String category;
	private String locationName;
	private Double latitude;
	private Double longitude;
	private boolean locationDetected;
}
