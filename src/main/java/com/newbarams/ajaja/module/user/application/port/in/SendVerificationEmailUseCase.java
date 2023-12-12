package com.newbarams.ajaja.module.user.application.port.in;

public interface SendVerificationEmailUseCase {
	void sendVerification(Long userId, String email);
}
