package me.ajaja.global.security.jwt;

import static me.ajaja.common.extenstion.AssertExtension.*;
import static me.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import me.ajaja.common.annotation.RedisBasedTest;
import me.ajaja.common.support.MonkeySupport;
import me.ajaja.global.security.common.CustomUserDetailsService;
import me.ajaja.global.security.common.UserAdapter;
import me.ajaja.module.auth.dto.AuthResponse;

@RedisBasedTest
class JwtParserTest extends MonkeySupport {
	@Autowired
	private JwtParser jwtParser;
	@Autowired
	private JwtGenerator jwtGenerator;
	@Autowired
	private JwtSecretProvider jwtSecretProvider;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@MockBean
	private CustomUserDetailsService customUserDetailsService;

	private Long userId;
	private String accessToken;
	private String refreshToken;

	@BeforeEach
	void setup() {
		userId = sut.giveMeOne(Long.class);

		AuthResponse.Token tokens = jwtGenerator.login(userId);

		accessToken = tokens.getAccessToken();
		refreshToken = tokens.getRefreshToken();
	}

	@AfterEach
	void clearCache() {
		redisTemplate.delete(jwtSecretProvider.getSignature() + userId);
	}

	@Test
	@DisplayName("JWT에서 Authentication를 파싱하면 UsernamePasswordAuthenticationToken이 생성되어야 한다.")
	void parseAuthentication_Success_WithSameUserAdapter() {
		// given
		UserAdapter adapter = sut.giveMeOne(UserAdapter.class);
		given(customUserDetailsService.loadUserByUsername(anyString())).willReturn(adapter);

		// when
		Authentication authentication = jwtParser.parseAuthentication(accessToken);

		// then
		then(customUserDetailsService).should(times(1)).loadUserByUsername(anyString());
		assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken.class);
		assertThat(authentication.getPrincipal()).isEqualTo(adapter);
	}

	@Test
	@DisplayName("Access Token에서 ID를 파싱하면 생성했을 때와 ID가 동일하고 예외가 발생하지 않아야 한다.")
	void parseId_Success_WithAccessToken_WithSameUserId() {
		// given

		// when, then
		assertThatNoException().isThrownBy(() -> {
			Long parsedId = jwtParser.parseId(accessToken);
			assertThat(userId).isEqualTo(parsedId);
		});
	}

	@Test
	@DisplayName("Refresh Token에서 ID를 파싱하면 생성했을 때와 ID가 동일하고 예외가 발생하지 않아야 한다.")
	void parseId_Success_WithRefreshToken_WithSameUserId() {
		// given

		// when, then
		assertThatNoException().isThrownBy(() -> {
			Long parsedId = jwtParser.parseId(refreshToken);
			assertThat(userId).isEqualTo(parsedId);
		});
	}

	@Test
	@DisplayName("서명이 다른 토큰을 파싱 시도하면 예외가 발생한다.")
	void parseId_Fail_ByWrongSignature() {
		// given
		String wrongSignatureToken = """
			eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.
			eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.
			SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
			""";

		// when, then
		assertThatException().isThrownBy(() -> jwtParser.parseId(wrongSignatureToken));
	}

	@Test
	@DisplayName("빈 토큰을 파싱 시도하면 예외가 발생한다.")
	void parseId_Fail_ByEmptyToken() {
		// given
		String emptyToken = "";

		// when, then
		assertThatAjajaException(EMPTY_TOKEN).isThrownBy(() ->
			jwtParser.parseId(emptyToken)
		);
	}

	@Test
	@DisplayName("Access Token만 유효해도 검증에 성공해야 한다.")
	void parseIdIfReissueable_Success_ByOnlyAccessTokenIsValid() {
		// given

		// when, then
		assertThatNoException().isThrownBy(() ->
			jwtParser.parseIdIfReissueable(accessToken, null)
		);
	}

	@Test
	@DisplayName("Refresh Token만 유효해도 검증에 성공해야 한다.")
	void parseIdIfReissueable_Success_ByOnlyRefreshTokenIsValid() {

		// when, then
		assertThatNoException().isThrownBy(() ->
			jwtParser.parseIdIfReissueable(null, refreshToken)
		);
	}

	@Test
	@DisplayName("Refresh Token만으로 검증할 때 로그인 이력이 존재하지 않으면 검증에 실패해야 한다.")
	void parseIdIfReissueable_Fail_ByNoneLoginHistory() {
		// given
		redisTemplate.delete(jwtSecretProvider.getSignature() + userId);

		// when, then
		assertThatAjajaException(NEVER_LOGIN).isThrownBy(() ->
			jwtParser.parseIdIfReissueable(null, refreshToken)
		);
	}

	@Test
	@DisplayName("검증을 요청한 Refresh Token과 관리 중인 Token이 다르다면 검증에 실패해야 한다..")
	void validateReissuable_Fail_ByDifferentRefreshToken() {
		// given
		String anotherToken = """
				eyJhbGciOiJIUzUxMiJ9.
				eyJleHAiOjk5OTk5OTk5OTl9.
				MY8pP9aep_3Dwza-unK3EmnPYJ88mYQe0IWjO_iMlbhKMcAzNpCmD11A9K--o_Pw6dc6slxnlb7zHNAVOUNsOw
			""";

		redisTemplate.opsForValue().set(jwtSecretProvider.getSignature() + userId, anotherToken);

		// when, then
		assertThatAjajaException(TOKEN_NOT_MATCH).isThrownBy(() ->
			jwtParser.parseIdIfReissueable(null, refreshToken)
		);
	}
}
