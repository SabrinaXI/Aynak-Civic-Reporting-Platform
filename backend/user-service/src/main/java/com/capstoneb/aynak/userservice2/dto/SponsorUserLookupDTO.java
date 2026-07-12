package com.capstoneb.aynak.userservice2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SponsorUserLookupDTO {

	private Integer sponsorUserId;
	private String email;
	private Integer sponsorId;
	private String firstName;
	private String lastName;
}
