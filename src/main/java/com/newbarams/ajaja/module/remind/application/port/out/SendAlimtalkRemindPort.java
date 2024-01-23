package com.newbarams.ajaja.module.remind.application.port.out;

public interface SendAlimtalkRemindPort {
	void send(String to, String planName, String message, String feedbackUrl);
}
