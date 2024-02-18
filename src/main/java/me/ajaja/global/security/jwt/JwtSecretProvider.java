package me.ajaja.global.security.jwt;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Getter
@Component
class JwtSecretProvider {
	private static final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L; // 7일
	private static final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L; // 30분

	private static final String DATE_CLAIM_POSTFIX = " DATE";

	private final SecretKey secretKey;
	private final String signature;

	public JwtSecretProvider(
		@Value("${secret.jwt.key}") String key,
		@Value("${secret.jwt.signature}") String signature
	) {
		byte[] keyBytes = Decoders.BASE64.decode(key);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
		this.signature = signature;
	}

	public String cacheKey(Long userId) {
		return signature + userId;
	}

	public String dateKey() {
		return signature + DATE_CLAIM_POSTFIX;
	}

	public long refreshTokenExpireIn() {
		return REFRESH_TOKEN_VALID_TIME;
	}

	public long accessTokenExpireIn() {
		return ACCESS_TOKEN_VALID_TIME;
	}
}
