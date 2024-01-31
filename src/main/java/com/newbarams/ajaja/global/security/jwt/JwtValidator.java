package com.newbarams.ajaja.global.security.jwt;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.cache.CacheUtil;
import com.newbarams.ajaja.global.exception.AjajaException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidator {
	private final JwtSecretProvider jwtSecretProvider;
	private final CacheUtil cacheUtil;
	private final JwtParser parser;

	/**
	 * Validation succeeds if either of the parameters is valid
	 * @author hejow
	 */
	public Long validateReissuableAndExtractId(String accessToken, String refreshToken) {
		return isValidToken(accessToken) ? parser.parseId(accessToken) : validateHistory(refreshToken);
	}

	private boolean isValidToken(String accessToken) {
		return parser.isParsable(accessToken);
	}

	private Long validateHistory(String refreshToken) {
		Long userId = parser.parseId(refreshToken);
		String savedToken = getSavedFromCache(userId);
		compare(savedToken, refreshToken);
		return userId;
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
