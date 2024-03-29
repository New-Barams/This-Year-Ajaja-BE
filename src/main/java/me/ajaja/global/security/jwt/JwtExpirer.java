package me.ajaja.global.security.jwt;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExpirer {
	private final JwtSecretProvider secretProvider;
	private final TokenStorage tokenStorage;

	public void expire(Long userId) {
		if (!tokenStorage.remove(secretProvider.cacheKey(userId))) {
			log.warn("[JWT] Refresh Token DELETED failed. ID : " + userId);
		}
	}
}
