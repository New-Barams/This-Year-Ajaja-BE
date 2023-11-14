package com.newbarams.ajaja.module.remind.application;

public interface SendEmailRemindService {
	void send(String email, String message, Long planId);
}
