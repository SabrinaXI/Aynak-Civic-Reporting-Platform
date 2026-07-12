package com.capstoneb.aynak.userservice2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnsureCitizenRequest {

	private String email;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String emirate;
	private String preferredLanguage;
	private String dateOfBirth;
}
