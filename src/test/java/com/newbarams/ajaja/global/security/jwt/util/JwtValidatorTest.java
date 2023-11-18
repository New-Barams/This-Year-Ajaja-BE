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
	@DisplayName("유효한 refresh token을 입력하면 검증에 성공한다.")
	void validate_Success_ByValidToken() {
		Long userId = monkey.giveMeOne(Long.class);
		UserResponse.Token tokens = jwtGenerator.generate(userId);

		// when, then
		assertThatNoException().isThrownBy(() -> jwtValidator.validateReissueable(userId, tokens.refreshToken()));
	}

	@Test
	@DisplayName("서명이 다른 토큰을 입력하면 검증에 실패한다.")
	void validate_Fail_ByWrongSignature() {
		// given
		Long userId = monkey.giveMeOne(Long.class);
		String wrongSignatureToken = """
			eyJhbGciOiJIUzI1NiJ9.
			eyJuYW1lIjoiSGVqb3cifQ.
			SI7XBRHE_95nkxQ69SiiCQcqDkZ-FW1RdxNL1DmAAAg
			""";

		// when, then
		assertThatException().isThrownBy(() -> jwtValidator.validateReissueable(userId, wrongSignatureToken));
	}

	@Test
	@DisplayName("로그인 이력 없이 refresh token을 입력하면 검증에 실패한다.")
	void validate_Fail_ByNoneLoginHistory() {
		// given
		Long userId = monkey.giveMeOne(Long.class);
		UserResponse.Token tokens = jwtGenerator.generate(userId);

		redisTemplate.delete(jwtSecretProvider.getSignature() + userId);

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> jwtValidator.validateReissueable(userId, tokens.refreshToken()))
			.withMessage(NEVER_LOGIN.getMessage());
	}

	@Test
	@DisplayName("관리 중인 refresh token과 다른 token을 입력하면 검증에 실패해야 한다.")
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
			.isThrownBy(() -> jwtValidator.validateReissueable(userId, oldTokens.refreshToken()))
			.withMessage(TOKEN_NOT_MATCH.getMessage());
	}
}
