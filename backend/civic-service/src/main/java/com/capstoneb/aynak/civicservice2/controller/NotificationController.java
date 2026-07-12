package com.capstoneb.aynak.civicservice2.controller;

import com.capstoneb.aynak.civicservice2.client.CitizenContextResolver;
import com.capstoneb.aynak.civicservice2.dto.CitizenContext;
import com.capstoneb.aynak.civicservice2.model.Notification;
import com.capstoneb.aynak.civicservice2.service.NotificationService;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	private final NotificationService notificationService;
	private final CitizenContextResolver citizenContextResolver;

	public NotificationController(NotificationService notificationService, CitizenContextResolver citizenContextResolver) {
		this.notificationService = notificationService;
		this.citizenContextResolver = citizenContextResolver;
	}

	@GetMapping("/my")
	public List<Notification> getMyNotifications(Authentication authentication) {
		return notificationService.getMyNotifications(citizenContext(authentication));
	}

	@PutMapping("/my/read")
	public String markMyNotificationsAsRead(Authentication authentication) {
		notificationService.markMyNotificationsAsRead(citizenContext(authentication));
		return "Notifications marked as read";
	}

	private CitizenContext citizenContext(Authentication authentication) {
		return citizenContextResolver.resolve((Jwt) authentication.getPrincipal());
	}
}
