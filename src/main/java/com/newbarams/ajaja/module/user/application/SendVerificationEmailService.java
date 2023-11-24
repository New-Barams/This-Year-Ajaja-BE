package com.newbarams.ajaja.module.user.application;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.user.application.model.EmailVerification;
import com.newbarams.ajaja.module.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SendVerificationEmailService {
	private static final long VERIFICATION_EMAIL_EXPIRE_IN = 10 * 60 * 1000L; // 10분
	private static final String KEY_PREFIX = "AJAJA ";

	private final SendCertificationService sendCertificationService;
	private final RetrieveUserService retrieveUserService;
	private final RedisTemplate<String, Object> redisTemplate;

	public void sendVerification(Long userId, String email) {
		validateEmail(userId, email);
		String certification = RandomCertificationGenerator.generate();
		sendCertificationService.send(email, certification);
		saveVerificationOnCache(userId, email, certification);
	}

	private void validateEmail(Long id, String email) {
		User user = retrieveUserService.loadExistUserById(id);
		user.validateEmail(email);
	}

	private void saveVerificationOnCache(Long userId, String email, String certification) {
		redisTemplate.opsForValue().set(
			KEY_PREFIX + userId,
			new EmailVerification(email, certification),
			Duration.ofMillis(VERIFICATION_EMAIL_EXPIRE_IN)
		);
	}
}
