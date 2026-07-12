package com.capstoneb.aynak.civicservice2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitizenLookupDTO {

	private Integer citizenId;
	private String email;
	private String firstName;
	private String lastName;
}
