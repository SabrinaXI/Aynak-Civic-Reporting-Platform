package com.capstoneb.aynak.civicservice2.service;

import com.capstoneb.aynak.civicservice2.dto.CitizenContext;
import com.capstoneb.aynak.civicservice2.model.Notification;
import com.capstoneb.aynak.civicservice2.model.Report;

import java.util.List;

public interface NotificationService {

	void notifyCitizen(Report report, String message);

	List<Notification> getMyNotifications(CitizenContext citizen);

	void markMyNotificationsAsRead(CitizenContext citizen);
}
