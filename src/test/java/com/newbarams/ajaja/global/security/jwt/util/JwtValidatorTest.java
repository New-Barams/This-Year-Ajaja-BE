package com.newbarams.ajaja.global.security.jwt.util;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.newbarams.ajaja.common.annotation.RedisBasedTest;
import com.newbarams.ajaja.common.support.MonkeySupport;
import com.newbarams.ajaja.global.exception.AjajaException;
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

	private Long userId;
	private UserResponse.Token tokens;

	@BeforeEach
	void setup() {
		userId = sut.giveMeOne(Long.class);
		tokens = jwtGenerator.login(userId);
	}

	@AfterEach
	void clearCache() {
		redisTemplate.delete(jwtSecretProvider.getSignature() + userId);
	}

	@Test
	@DisplayName("유효한 Access Token만으로도 검증해도 성공해야 한다.")
	void validateReissuable_Success_ByAccessToken() {
		// given

		// when, then
		assertThatNoException()
			.isThrownBy(() -> jwtValidator.validateReissuableAndExtractId(tokens.getAccessToken(), null));
	}

	@Test
	@DisplayName("유효한 Refresh Token만으로도 검증해도 성공해야 한다.")
	void validateReissuable_Success_ByRefreshToken() {

		// when, then
		assertThatNoException()
			.isThrownBy(() -> jwtValidator.validateReissuableAndExtractId(null, tokens.getRefreshToken()));
	}

	@Test
	@DisplayName("Refresh Token으로 검증 시 로그인 이력이 존재하지 않으면 검증에 실패해야 한다.")
	void validateReissuable_Fail_ByNoneLoginHistory() {
		// given
		redisTemplate.delete(jwtSecretProvider.getSignature() + userId);

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> jwtValidator.validateReissuableAndExtractId(null, tokens.getRefreshToken()))
			.withMessage(NEVER_LOGIN.getMessage());
	}

	@Test
	@DisplayName("검증을 요청한 Refresh Token과 관리 중인 Token이 다르다면 검증에 실패해야 한다..")
	void validateReissuable_Fail_ByDifferentRefreshToken() {
		// given
		String newToken = """
			eyJhbGciOiJIUzUxMiJ9.
			eyJleHAiOjk5OTk5OTk5OTl9.
			MY8pP9aep_3Dwza-unK3EmnPYJ88mYQe0IWjO_iMlbhKMcAzNpCmD11A9K--o_Pw6dc6slxnlb7zHNAVOUNsOw
			""";

		redisTemplate.opsForValue().set(jwtSecretProvider.getSignature() + userId, newToken);

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> jwtValidator.validateReissuableAndExtractId(null, tokens.getRefreshToken()))
			.withMessage(TOKEN_NOT_MATCH.getMessage());
	}
}
