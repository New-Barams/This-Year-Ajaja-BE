package com.newbarams.ajaja.global.security.jwt.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.newbarams.ajaja.common.annotation.RedisBasedTest;
import com.newbarams.ajaja.common.support.MonkeySupport;

@RedisBasedTest
class JwtRemoverTest extends MonkeySupport {
	@Autowired
	private JwtRemover jwtRemover;

	@Autowired
	private JwtGenerator jwtGenerator;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private JwtSecretProvider jwtSecretProvider;

	@Test
	@DisplayName("JWT를 제거하면 캐시에 아무것도 조회되지 않아야 한다.")
	void remove_Success() {
		// given
		Long userId = sut.giveMeOne(Long.class);
		jwtGenerator.generate(userId);

		// when
		jwtRemover.remove(userId);

		// then
		assertThat(redisTemplate.opsForValue().get(jwtSecretProvider.getSignature() + userId)).isNull();
	}

}
