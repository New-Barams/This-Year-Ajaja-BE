package com.newbarams.ajaja.module.user.application.port.in;

public interface VerifyCertificationUseCase {
	/**
	 * verify email with generated certification number which made by server
	 * @param userId
	 * @param certification six-digit certification number
	 */
	void verify(Long userId, String certification);
}
