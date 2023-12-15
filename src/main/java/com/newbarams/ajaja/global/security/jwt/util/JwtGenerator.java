package com.newbarams.ajaja.global.security.jwt.util;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.cache.CacheUtil;
import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.user.dto.UserResponse;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtGenerator {
	private static final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L; // 30분
	private static final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일

	private final JwtSecretProvider jwtSecretProvider;
	private final CacheUtil cacheUtil;

	public UserResponse.Token generate(Long userId) {
		final TimeValue time = new TimeValue();
		String accessToken = generateJwt(userId, time.expireIn(ACCESS_TOKEN_VALID_TIME));
		String refreshToken = generateJwt(userId, time.expireIn(REFRESH_TOKEN_VALID_TIME));

		cacheUtil.saveRefreshToken(jwtSecretProvider.cacheKey(userId), refreshToken, REFRESH_TOKEN_VALID_TIME);
		return new UserResponse.Token(accessToken, refreshToken, time.getTimeMillis() + ACCESS_TOKEN_VALID_TIME);
	}

	private String generateJwt(Long userId, Date expireIn) {
		return Jwts.builder()
			.claim(jwtSecretProvider.getSignature(), userId)
			.expiration(expireIn)
			.signWith(jwtSecretProvider.getSecretKey())
			.compact();
	}
}
