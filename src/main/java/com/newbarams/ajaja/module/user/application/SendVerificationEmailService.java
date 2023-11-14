package com.newbarams.ajaja.module.user.application;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SendVerificationEmailService {
	private static final long VERIFICATION_EMAIL_EXPIRE_IN = 10 * 60 * 1000L; // 10분
	private static final String KEY_PREFIX = "AJAJA ";

	private final SendCertificationService sendCertificationService;
	private final UpdateRemindEmailService updateRemindEmailService;
	private final RedisTemplate<String, Object> redisTemplate;

	public void sendVerification(Long id, String email) {
		updateRemindEmailService.updateIfDifferent(id, email);
		String certification = RandomCertificationGenerator.generate();
		sendCertificationService.send(email, certification);
		saveVerificationOnCache(email, certification);
	}

	private void saveVerificationOnCache(String email, String certification) {
		redisTemplate.opsForValue().set(
			KEY_PREFIX + email,
			certification,
			VERIFICATION_EMAIL_EXPIRE_IN,
			TimeUnit.MILLISECONDS
		);
	}
}
