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

	public String getDateKey() {
		return signature + DATE_CLAIM_POSTFIX;
	}
}
