package com.newbarams.ajaja.module.remind.application;

import org.springframework.stereotype.Service;

@Service
public interface SendPlanRemindService {
	void send(String email, String title, String message, Long feedbackId);
}
