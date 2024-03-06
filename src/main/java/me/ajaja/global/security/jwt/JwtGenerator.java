package me.ajaja.global.security.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.BaseTime;
import me.ajaja.module.auth.dto.AuthResponse;

@Component
@RequiredArgsConstructor
public class JwtGenerator {
	@FunctionalInterface
	private interface RefreshTokenGenerator {
		String generate(Long userId, BaseTime time);
	}

	private final JwtSecretProvider secretProvider;
	private final TokenStorage tokenStorage;
	private final RawParser rawParser;

	public AuthResponse.Token login(Long userId) {
		return generate(userId, this::generateRefresh);
	}

	public AuthResponse.Token reissue(Long userId, String oldToken) {
		return generate(userId, (id, time) -> is3DaysLeft(oldToken, time) ? generateRefresh(id, time) : oldToken);
	}

	private AuthResponse.Token generate(Long userId, RefreshTokenGenerator refreshTokenGenerator) {
		final BaseTime now = BaseTime.now();

		return new AuthResponse.Token(
			generateAccess(userId, now),
			refreshTokenGenerator.generate(userId, now),
			now.getTimeMillis() + secretProvider.accessTokenExpireIn()
		);
	}

	private String generateAccess(Long userId, BaseTime time) {
		Date accessTokenExpireIn = time.expireIn(secretProvider.accessTokenExpireIn());

		return Jwts.builder()
			.claim(secretProvider.getSignature(), userId)
			.expiration(accessTokenExpireIn)
			.signWith(secretProvider.getSecretKey())
			.compact();
	}

	private String generateRefresh(Long userId, BaseTime time) {
		Date refreshTokenExpireIn = time.expireIn(secretProvider.refreshTokenExpireIn());

		String refreshToken = Jwts.builder()
			.claim(secretProvider.getSignature(), userId)
			.claim(secretProvider.dateKey(), refreshTokenExpireIn)
			.expiration(refreshTokenExpireIn)
			.signWith(secretProvider.getSecretKey())
			.compact();

		tokenStorage.save(secretProvider.cacheKey(userId), refreshToken, secretProvider.refreshTokenExpireIn());
		return refreshToken;
	}

	private boolean is3DaysLeft(String oldRefreshToken, BaseTime time) {
		Date expireIn = rawParser.parseClaimWithType(oldRefreshToken, secretProvider.dateKey(), Date.class);
		return time.isWithin3Days(expireIn);
	}
}
