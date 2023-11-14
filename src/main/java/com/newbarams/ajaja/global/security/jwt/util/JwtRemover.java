package com.newbarams.ajaja.global.security.jwt.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRemover {
	private final RedisTemplate<String, Object> redisTemplate;
	private final JwtSecretProvider jwtSecretProvider;

	public void remove(Long userId) {
		redisTemplate.delete(jwtSecretProvider.getSignature() + userId);
	}
}
