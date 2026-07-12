package com.capstoneb.aynak.userservice2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * One row per applied points adjustment. The (citizenId, reason, referenceId) unique
 * constraint makes adjustPoints idempotent: a retried Feign call for the same report
 * approval or reward redemption won't double-apply.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "citizen_id", "reason", "reference_id" }))
public class PointsLedger {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer citizenId;

	private int delta;

	private String reason;

	private Integer referenceId;

	private LocalDateTime createdAt;
}
