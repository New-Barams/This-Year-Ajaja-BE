package com.newbarams.ajaja.global.security.jwt.util;

import static com.newbarams.ajaja.global.common.error.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.newbarams.ajaja.common.MonkeySupport;
import com.newbarams.ajaja.common.RedisBasedTest;
import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.module.user.dto.UserResponse;

@RedisBasedTest
class JwtValidatorTest extends MonkeySupport {
	@Autowired
	private JwtValidator jwtValidator;

	@Autowired
	private JwtGenerator jwtGenerator;

	@Autowired
	private JwtSecretProvider jwtSecretProvider;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Test
	@DisplayName("토큰을 검증할 때 accessToken만으로도 검증되어야 한다.")
	void validate_Success_ByAccessToken() {
		// given
		Long userId = monkey.giveMeOne(Long.class);
		UserResponse.Token tokens = jwtGenerator.generate(userId);

		// when, then
		assertThatNoException().isThrownBy(() -> jwtValidator.validate(userId, tokens.accessToken(), null));
	}

	@Test
	@DisplayName("토큰을 검증할 때 refreshToken만으로도 검증되어야 한다.")
	void validate_Success_ByRefreshToken() {
		Long userId = monkey.giveMeOne(Long.class);
		UserResponse.Token tokens = jwtGenerator.generate(userId);

		// when, then
		assertThatNoException().isThrownBy(() -> jwtValidator.validate(userId, null, tokens.refreshToken()));
	}

	@Test
	@DisplayName("서명이 다른 토큰을 넣으면 검증에 실패한다.")
	void validate_Fail_ByInvalidToken() {
		// given
		Long userId = monkey.giveMeOne(Long.class);
		String wrongSignatureToken = """
			eyJhbGciOiJIUzI1NiJ9.
			eyJuYW1lIjoiSGVqb3cifQ.
			SI7XBRHE_95nkxQ69SiiCQcqDkZ-FW1RdxNL1DmAAAg
			""";

		// when, then
		assertThatException().isThrownBy(() -> jwtValidator.validate(userId, wrongSignatureToken, null));
		assertThatException().isThrownBy(() -> jwtValidator.validate(userId, null, wrongSignatureToken));
	}

	@Test
	@DisplayName("Refresh 토큰이 만료된 경우 검증에 실패해야 한다.")
	void validate_Fail_ByTokenExpired() {
		// given
		Long userId = monkey.giveMeOne(Long.class);
		UserResponse.Token tokens = jwtGenerator.generate(userId);

		redisTemplate.delete(jwtSecretProvider.getSignature() + userId);

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> jwtValidator.validate(userId, null, tokens.refreshToken()))
			.withMessage(EXPIRED_TOKEN.getMessage());
	}

	@Test
	@DisplayName("관리 중인 Refresh 토큰과 다른 토큰이 입력되면 검증에 실패해야 한다.")
	void validate_Fail_ByDifferentRefreshToken() {
		// given
		Long userId = monkey.giveMeOne(Long.class);
		UserResponse.Token oldTokens = jwtGenerator.generate(userId);

		String newToken = """
			eyJhbGciOiJIUzUxMiJ9.
			eyJleHAiOjk5OTk5OTk5OTl9.
			MY8pP9aep_3Dwza-unK3EmnPYJ88mYQe0IWjO_iMlbhKMcAzNpCmD11A9K--o_Pw6dc6slxnlb7zHNAVOUNsOw
			""";

		redisTemplate.opsForValue().set(jwtSecretProvider.getSignature() + userId, newToken);

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> jwtValidator.validate(userId, null, oldTokens.refreshToken()))
			.withMessage(TOKEN_NOT_MATCH.getMessage());
	}
}
