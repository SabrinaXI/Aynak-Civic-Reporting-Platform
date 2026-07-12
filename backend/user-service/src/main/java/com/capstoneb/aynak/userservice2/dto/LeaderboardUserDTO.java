package com.capstoneb.aynak.userservice2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardUserDTO {

	private Integer userId;
	private String fullName;
	private int approvedReports;
	private int rank;
}
