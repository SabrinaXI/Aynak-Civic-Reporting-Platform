package com.capstoneb.aynak.civicservice2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Reward {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String brandName;

	private String title;

	private String description;

	private String voucherCode;

	private int pointsRequired;

	private boolean active = true;

	// plain FK to user-service2's Sponsor, not a JPA relationship (different database)
	private Integer sponsorId;
}
