package com.newbarams.ajaja.module.user.application.port.in;

public interface SendVerificationEmailUseCase {
	/**
	 * send verify email to requested email
	 * @param userId
	 * @param email destination to send verification
	 */
	void sendVerification(Long userId, String email);
}
