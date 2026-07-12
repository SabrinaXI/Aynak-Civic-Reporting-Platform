package com.capstoneb.aynak.civicservice2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdjustPointsRequest {

	private int delta;
	private String reason;
	private Integer referenceId;
}
