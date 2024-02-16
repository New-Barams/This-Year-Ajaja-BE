package me.ajaja.global.security.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.TimeValue;
import me.ajaja.module.auth.dto.AuthResponse;

@Component
@RequiredArgsConstructor
public class JwtGenerator {
	@FunctionalInterface
	private interface RefreshTokenGenerator {
		String generate(Long userId, TimeValue time);
	}

	private final JwtSecretProvider secretProvider;
	private final TokenCache tokenCache;
	private final RawParser rawParser;

	public AuthResponse.Token login(Long userId) {
		return generate(userId, this::generateRefresh);
	}

	public AuthResponse.Token reissue(Long userId, String oldToken) {
		return generate(userId, (id, time) -> is3DaysLeft(oldToken, time) ? generateRefresh(id, time) : oldToken);
	}

	private AuthResponse.Token generate(Long userId, RefreshTokenGenerator refreshTokenGenerator) {
		final TimeValue now = TimeValue.now();

		return new AuthResponse.Token(
			generateAccess(userId, now),
			refreshTokenGenerator.generate(userId, now),
			now.getTimeMillis() + secretProvider.accessTokenExpireIn()
		);
	}

	private String generateAccess(Long userId, TimeValue time) {
		Date accessTokenExpireIn = time.expireIn(secretProvider.accessTokenExpireIn());

		return Jwts.builder()
			.claim(secretProvider.getSignature(), userId)
			.expiration(accessTokenExpireIn)
			.signWith(secretProvider.getSecretKey())
			.compact();
	}

	private String generateRefresh(Long userId, TimeValue time) {
		Date refreshTokenExpireIn = time.expireIn(secretProvider.refreshTokenExpireIn());

		String refreshToken = Jwts.builder()
			.claim(secretProvider.getSignature(), userId)
			.claim(secretProvider.dateKey(), refreshTokenExpireIn)
			.expiration(refreshTokenExpireIn)
			.signWith(secretProvider.getSecretKey())
			.compact();

		tokenCache.save(secretProvider.cacheKey(userId), refreshToken, secretProvider.refreshTokenExpireIn());
		return refreshToken;
	}

	private boolean is3DaysLeft(String oldRefreshToken, TimeValue time) {
		Date expireIn = rawParser.parseClaimWithType(oldRefreshToken, secretProvider.dateKey(), Date.class);
		return time.isWithin3Days(expireIn);
	}
}
