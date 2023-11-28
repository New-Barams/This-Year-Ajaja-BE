package com.newbarams.ajaja.global.security.jwt.util;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.cache.CacheUtil;
import com.newbarams.ajaja.global.exception.AjajaException;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Component
public class JwtValidator {
	private final JwtSecretProvider jwtSecretProvider;
	private final CacheUtil cacheUtil;
	private final JwtParser parser;

	JwtValidator(JwtSecretProvider jwtSecretProvider, CacheUtil cacheUtil) {
		this.jwtSecretProvider = jwtSecretProvider;
		this.cacheUtil = cacheUtil;
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
		if (cacheUtil.getRefreshToken(jwtSecretProvider.cacheKey(userId)) instanceof String token) {
			return token;
		}

		throw new AjajaException(NEVER_LOGIN);
	}

	private void compare(String saved, String input) {
		if (!saved.equals(input)) {
			throw new AjajaException(TOKEN_NOT_MATCH);
		}
	}
}
