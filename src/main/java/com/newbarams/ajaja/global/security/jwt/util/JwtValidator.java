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

	public void validateReissueable(Long userId, String refreshToken) {
		validateToken(refreshToken);
		validateHistory(userId, refreshToken);
	}

	private void validateToken(String jwt) {
		try {
			parser.parseSignedClaims(jwt);
		} catch (JwtException | IllegalArgumentException e) {
			throw new AjajaException(INVALID_TOKEN);
		}
	}

	private void validateHistory(Long userId, String refreshToken) {
		String savedToken = getSavedFromCache(userId);
		compare(savedToken, refreshToken);
	}

	private String getSavedFromCache(Long userId) {
		Object saved = redisTemplate.opsForValue().get(jwtSecretProvider.getSignature() + userId);
		validateExists(saved);
		return (String)saved;
	}

	private void validateExists(Object refreshToken) {
		if (refreshToken == null) {
			throw new AjajaException(NEVER_LOGIN);
		}
	}

	private void compare(String saved, String input) {
		if (!saved.equals(input)) {
			throw new AjajaException(TOKEN_NOT_MATCH);
		}
	}
}
