package com.newbarams.ajaja.module.remind.application.port.out;

import org.springframework.stereotype.Service;

@Service
public interface SendEmailRemindPort {
	void send(String email, String title, String message, String feedbackUrl);
}
