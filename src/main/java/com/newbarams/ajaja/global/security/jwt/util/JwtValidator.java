package com.newbarams.ajaja.global.security.jwt.util;

import static com.newbarams.ajaja.global.common.error.ErrorCode.*;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.common.exception.AjajaException;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Component
public class JwtValidator {
	private final JwtSecretProvider jwtSecretProvider;
	private final RedisTemplate<String, Object> redisTemplate;
	private final JwtParser parser;

	JwtValidator(JwtSecretProvider jwtSecretProvider, RedisTemplate<String, Object> redisTemplate) {
		this.jwtSecretProvider = jwtSecretProvider;
		this.redisTemplate = redisTemplate;
		this.parser = Jwts.parser().verifyWith(jwtSecretProvider.getSecretKey()).build();
	}

	public void validate(Long userId, String accessToken, String refreshToken) {
		if (isValid(accessToken)) {
			return;
		}

		validateRefreshToken(userId, refreshToken);
	}

	private boolean isValid(String jwt) {
		try {
			parser.parseSignedClaims(jwt);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private void validateRefreshToken(Long userId, String refreshToken) {
		if (!isValid(refreshToken)) {
			throw new AjajaException(INVALID_TOKEN);
		}

		compareWithSaved(userId, refreshToken);
	}

	private void compareWithSaved(Long userId, String refreshToken) {
		String savedToken = getSavedOnCache(userId);
		compare(savedToken, refreshToken);
	}

	private String getSavedOnCache(Long userId) {
		Object saved = redisTemplate.opsForValue().get(jwtSecretProvider.getSignature() + userId);
		validateExpired(saved);
		return (String)saved;
	}

	private void validateExpired(Object refreshToken) {
		if (refreshToken == null) {
			throw new AjajaException(EXPIRED_TOKEN);
		}
	}

	private void compare(String saved, String input) {
		if (!saved.equals(input)) {
			throw new AjajaException(TOKEN_NOT_MATCH);
		}
	}
}
