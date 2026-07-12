package com.capstoneb.aynak.civicservice2.service.impl;

import com.capstoneb.aynak.civicservice2.dto.CitizenContext;
import com.capstoneb.aynak.civicservice2.model.Notification;
import com.capstoneb.aynak.civicservice2.model.Report;
import com.capstoneb.aynak.civicservice2.repository.NotificationRepo;
import com.capstoneb.aynak.civicservice2.service.EmailService;
import com.capstoneb.aynak.civicservice2.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepo notificationRepo;
	private final EmailService emailService;

	public NotificationServiceImpl(NotificationRepo notificationRepo, EmailService emailService) {
		this.notificationRepo = notificationRepo;
		this.emailService = emailService;
	}

	@Override
	public void notifyCitizen(Report report, String message) {

		Notification notification = new Notification();

		notification.setCitizenId(report.getCitizenId());
		notification.setReport(report);
		notification.setMessage(message);
		notification.setReadStatus(false);
		notification.setCreatedAt(LocalDateTime.now());

		notificationRepo.save(notification);

		emailService.sendEmail(report.getCitizenEmail(), "Aynak Report Update", message);
	}

	@Override
	public List<Notification> getMyNotifications(CitizenContext citizen) {
		return notificationRepo.findByCitizenIdOrderByCreatedAtDesc(citizen.getCitizenId());
	}

	@Override
	public void markMyNotificationsAsRead(CitizenContext citizen) {

		List<Notification> unreadNotifications = notificationRepo.findByCitizenIdAndReadStatusFalse(citizen.getCitizenId());

		for (Notification notification : unreadNotifications) {
			notification.setReadStatus(true);
		}

		notificationRepo.saveAll(unreadNotifications);
	}
}
