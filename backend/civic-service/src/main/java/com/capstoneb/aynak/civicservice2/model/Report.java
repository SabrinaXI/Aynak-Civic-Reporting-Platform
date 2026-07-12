package com.capstoneb.aynak.civicservice2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Report {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String title;

	@Column(length = 1000)
	private String description;

	private String category;

	private String locationName;

	private Double latitude;

	private Double longitude;

	// PENDING, APPROVED (not fake/spam), REJECTED, IN_PROGRESS (authority is resolving), RESOLVED
	private String status;

	private String photoContentType;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] photo;

	private LocalDateTime createdAt;

	// no cross-database JPA relationship to Citizen (owned by user-service2) - plain FK + a
	// denormalized email captured at creation time so status-change emails don't need a Feign call.
	private Integer citizenId;

	private String citizenEmail;
}
