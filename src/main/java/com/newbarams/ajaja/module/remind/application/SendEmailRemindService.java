package com.newbarams.ajaja.module.remind.application;

import org.springframework.stereotype.Service;

@Service
public interface SendEmailRemindService {
	void send(String email, String message, Long feedbackId);
}
