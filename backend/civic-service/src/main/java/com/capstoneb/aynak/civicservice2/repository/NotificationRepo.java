package com.capstoneb.aynak.civicservice2.repository;

import com.capstoneb.aynak.civicservice2.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Integer> {

	List<Notification> findByCitizenIdOrderByCreatedAtDesc(Integer citizenId);

	List<Notification> findByCitizenIdAndReadStatusFalse(Integer citizenId);
}
