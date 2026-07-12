package com.capstoneb.aynak.userservice2.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Authority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String authorityName;

	private String authorityCategory;

	private String contactEmail;

	@Column(length = 1000)
	private String description;

	private byte[] logo;

	private String logoContentType;

	private LocalDateTime createdAt;
}
