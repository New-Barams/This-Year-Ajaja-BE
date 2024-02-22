package me.ajaja.module.user.application;

public interface VerificationStorage {
	void save(Long userId, String email, String certification);

	String getEmailIfVerified(Long userId, String certification);
}
