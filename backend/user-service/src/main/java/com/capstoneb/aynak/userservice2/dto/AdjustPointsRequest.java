package com.capstoneb.aynak.userservice2.dto;

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
