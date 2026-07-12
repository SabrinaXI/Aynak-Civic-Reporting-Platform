package com.capstoneb.aynak.userservice2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportCountDTO {

	private Integer citizenId;
	private int approvedCount;
}
