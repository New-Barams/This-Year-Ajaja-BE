package com.newbarams.ajaja.global.cache;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.user.application.model.Verification;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CacheUtil {
	private static final long VERIFICATION_EMAIL_EXPIRE_IN = 10 * 60 * 1000L; // 10분
	private static final String DEFAULT_KEY = "AJAJA ";

	private final RedisTemplate<String, Object> redisTemplate;

	public void saveEmailVerification(Long userId, String email, String certification) {
		redisTemplate.opsForValue().set(
			DEFAULT_KEY + userId,
			new EmailVerification(email, certification),
			Duration.ofMillis(VERIFICATION_EMAIL_EXPIRE_IN)
		);
	}

	public Verification getEmailVerification(Long userId) {
		if (getSaveByDefaultKey(userId) instanceof EmailVerification verification) {
			return verification;
		}

		throw new AjajaException(CERTIFICATION_NOT_FOUND);
	}

	private Object getSaveByDefaultKey(Long userId) {
		return redisTemplate.opsForValue().get(DEFAULT_KEY + userId);
	}
}
