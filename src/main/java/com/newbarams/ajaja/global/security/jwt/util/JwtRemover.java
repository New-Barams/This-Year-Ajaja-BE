package com.newbarams.ajaja.global.security.jwt.util;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.global.cache.CacheUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
