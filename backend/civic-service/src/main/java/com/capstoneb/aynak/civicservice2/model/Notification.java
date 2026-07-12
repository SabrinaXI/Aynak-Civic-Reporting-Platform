package com.capstoneb.aynak.civicservice2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 1000)
	private String message;

	private boolean readStatus = false;

	private LocalDateTime createdAt;

	// plain FK to user-service2's Citizen, not a JPA relationship (different database)
	private Integer citizenId;

	@ManyToOne
	private Report report;
}
