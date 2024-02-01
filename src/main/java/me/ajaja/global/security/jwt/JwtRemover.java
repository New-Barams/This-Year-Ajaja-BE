package me.ajaja.global.security.jwt;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ajaja.global.cache.CacheUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRemover {
	private final JwtSecretProvider jwtSecretProvider;
	private final CacheUtil cacheUtil;

	public void remove(Long userId) {
		if (!cacheUtil.deleteRefreshToken(jwtSecretProvider.cacheKey(userId))) {
			log.info("[JWT] Refresh Token DELETED FAIL");
		}
	}
}
