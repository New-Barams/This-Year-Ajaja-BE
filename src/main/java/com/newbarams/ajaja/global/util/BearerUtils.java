package com.newbarams.ajaja.global.util;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import java.util.Objects;

import com.newbarams.ajaja.global.exception.AjajaException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BearerUtils {
	private static final String BEARER_PREFIX = "Bearer ";

	public static String toBearer(String token) {
		Objects.requireNonNull(token, "Token must be not null");
		return BEARER_PREFIX + token;
	}

	public static void validate(String token) {
		if (isNotBearer(token)) {
			throw new AjajaException(INVALID_BEARER_TOKEN);
		}
	}

	private static boolean isNotBearer(String token) {
		return token == null || !token.startsWith(BEARER_PREFIX);
	}

	public static String resolve(String bearerToken) {
		return bearerToken.substring(BEARER_PREFIX.length());
	}
}
