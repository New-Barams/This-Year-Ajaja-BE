package me.ajaja.global.cache;

import static me.ajaja.global.exception.ErrorCode.*;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;
import me.ajaja.global.security.jwt.TokenStorage;
import me.ajaja.module.user.application.VerificationStorage;

@Component
@RequiredArgsConstructor
class RedisCache implements TokenStorage, VerificationStorage {
	private static final long VERIFICATION_EMAIL_EXPIRE_IN = 10 * 60 * 1000L; // 10분
	private static final String DEFAULT_KEY = "AJAJA ";

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * email
	 */
	@Override
	public void save(Long userId, String email, String certification) {
		redisTemplate.opsForValue().set(
			DEFAULT_KEY + userId,
			new EmailVerification(email, certification),
			Duration.ofMillis(VERIFICATION_EMAIL_EXPIRE_IN)
		);
	}

	@Override
	public String getEmailIfVerified(Long userId, String certification) {
		EmailVerification verification = poll(DEFAULT_KEY + userId, EmailVerification.class);
		compare(verification.certification(), certification, CERTIFICATION_NOT_MATCH);
		return verification.email();
	}

	/**
	 * token
	 */
	@Override
	public void save(String key, String refreshToken, long expireIn) {
		redisTemplate.opsForValue().set(key, refreshToken, Duration.ofMillis(expireIn));
	}

	@Override
	public void validateHistory(String key, String refreshToken) {
		String token = poll(key, String.class);
		compare(token, refreshToken, TOKEN_NOT_MATCH);
	}

	@Override
	public boolean remove(String key) {
		return Boolean.TRUE.equals(redisTemplate.delete(key));
	}

	private <T> T poll(String key, Class<T> type) {
		Object saved = redisTemplate.opsForValue().get(key);

		if (type.isInstance(saved)) {
			return type.cast(saved);
		}

		throw new AjajaException(EMPTY_CACHE);
	}

	private void compare(String saved, String target, ErrorCode errorCode) {
		if (!saved.equals(target)) {
			throw new AjajaException(errorCode);
		}
	}
}
