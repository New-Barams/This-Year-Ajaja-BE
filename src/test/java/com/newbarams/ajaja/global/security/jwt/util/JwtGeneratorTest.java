package com.newbarams.ajaja.global.security.jwt.util;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.newbarams.ajaja.common.annotation.RedisBasedTest;
import com.newbarams.ajaja.common.support.MonkeySupport;
import com.newbarams.ajaja.module.user.dto.UserResponse;

@RedisBasedTest
class JwtGeneratorTest extends MonkeySupport {
	@Autowired
	private JwtGenerator jwtGenerator;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	private JwtSecretProvider jwtSecretProvider;

	@Test
	@DisplayName("JWT를 생성하면 DTO가 생성되면서 Refresh Token이 cache에 저장되어야 한다.")
	void generate_Success_WithRefreshTokenSavedOnRedis() {
		// given
		Long userId = sut.giveMeOne(Long.class);

		// when
		UserResponse.Token response = jwtGenerator.generate(userId);

		// then
		assertThat(response.getAccessToken()).isNotNull();
		assertThat(response.getRefreshToken()).isNotNull();

		Object savedToken = redisTemplate.opsForValue().get(jwtSecretProvider.getSignature() + userId);
		assertThat(savedToken).isNotNull();
		assertThat(savedToken).isInstanceOf(String.class);
		assertThat(response.getRefreshToken()).isEqualTo((String)savedToken);
		assertThat(response.getAccessTokenExpireIn()).isGreaterThan(new Date().getTime());
	}
}
