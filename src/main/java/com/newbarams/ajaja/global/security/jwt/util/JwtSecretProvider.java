package com.newbarams.ajaja.global.security.jwt.util;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Getter
@Component
class JwtSecretProvider {
	private final Key secretKey;
	private final String signature;

	public JwtSecretProvider(@Value("${secret.jwt.key}") String key) {
		byte[] keyBytes = Decoders.BASE64.decode(key);
		this.secretKey = Keys.hmacShaKeyFor(keyBytes);
		this.signature = "ajaja";
	}
}
