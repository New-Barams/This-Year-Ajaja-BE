package com.newbarams.ajaja.global.util;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BearerTokenUtil {
	private static final String BEARER_PREFIX = "Bearer ";

	public static String toBearer(String token) {
		Objects.requireNonNull(token, "Token must be not null");
		return BEARER_PREFIX + token;
	}

	public static boolean isBearer(String token) {
		return token.startsWith(BEARER_PREFIX);
	}

	public static String resolveJwt(String bearerToken) {
		return bearerToken.substring(BEARER_PREFIX.length());
	}
}
