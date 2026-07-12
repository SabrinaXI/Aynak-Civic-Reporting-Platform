package com.capstoneb.aynak.userservice2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Citizen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(unique = true)
	private String email;

	private String firstName;
	private String lastName;

	private String role;

	private LocalDate dateOfBirth;
	private String phoneNumber;
	private String emirate;
	private String preferredLanguage;

	private int aynakPoints = 0;

	private LocalDateTime createdAt;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] profilePicture;
}
