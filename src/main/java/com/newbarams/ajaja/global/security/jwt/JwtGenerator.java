package com.newbarams.ajaja.global.security.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.cache.CacheUtil;
import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.auth.dto.AuthResponse;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtGenerator {
	@FunctionalInterface
	private interface RefreshTokenGenerator {
		String generate(Long userId, TimeValue time);
	}

	private static final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L; // 30분
	private static final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

	private final JwtSecretProvider jwtSecretProvider;
	private final CacheUtil cacheUtil;
	private final JwtParser parser;

	public AuthResponse.Token login(Long userId) {
		return generate(userId, this::generateRefreshToken);
	}

	public AuthResponse.Token reissue(Long userId, String oldRefreshToken) {
		return generate(userId,
			(id, time) -> shouldReissue(oldRefreshToken, time) ? generateRefreshToken(id, time) : oldRefreshToken
		);
	}

	private AuthResponse.Token generate(Long userId, RefreshTokenGenerator generator) {
		final TimeValue now = TimeValue.now();
		String accessToken = generateAccessToken(userId, now);
		String refreshToken = generator.generate(userId, now);

		return new AuthResponse.Token(accessToken, refreshToken, now.getTimeMillis() + ACCESS_TOKEN_VALID_TIME);
	}

	private String generateAccessToken(Long userId, TimeValue time) {
		Date accessTokenExpireIn = time.expireIn(ACCESS_TOKEN_VALID_TIME);
		return Jwts.builder()
			.claim(jwtSecretProvider.getSignature(), userId)
			.expiration(accessTokenExpireIn)
			.signWith(jwtSecretProvider.getSecretKey())
			.compact();
	}

	private String generateRefreshToken(Long userId, TimeValue time) {
		Date refreshTokenExpireIn = time.expireIn(REFRESH_TOKEN_VALID_TIME);

		String refreshToken = Jwts.builder()
			.claim(jwtSecretProvider.getSignature(), userId)
			.claim(jwtSecretProvider.getDateKey(), refreshTokenExpireIn)
			.expiration(refreshTokenExpireIn)
			.signWith(jwtSecretProvider.getSecretKey())
			.compact();

		cacheUtil.saveRefreshToken(jwtSecretProvider.cacheKey(userId), refreshToken, REFRESH_TOKEN_VALID_TIME);
		return refreshToken;
	}

	private boolean shouldReissue(String oldRefreshToken, TimeValue time) {
		Date expireIn = parser.parseExpireIn(oldRefreshToken);
		return time.isWithinThreeDays(expireIn);
	}
}
