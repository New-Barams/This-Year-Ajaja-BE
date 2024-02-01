package me.ajaja.global.security.jwt;

import static me.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
		AuthResponse.Token response = jwtGenerator.login(userId);
		accessToken = response.getAccessToken();
		refreshToken = response.getRefreshToken();
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

	// @Test
	// @DisplayName("Refresh Token에서 만료일을 파싱하면 예외가 발생하지 않아야 한다.")
	// void parseExpireIn_Success_WithinOneWeek() {
	// 	// given
	// 	String expected = LocalDateTime.now().plusWeeks(1).toString();
	//
	// 	// when, then
	// 	assertThatNoException().isThrownBy(() -> {
	// 		Date expireIn = jwtParser.parseExpireIn(refreshToken);
	// 		assertThat(expireIn).isCloseTo(expected, 1000);
	// 	});
	// }

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
		assertThatException()
			.isThrownBy(() -> jwtParser.parseId(emptyToken))
			.withMessage(EMPTY_TOKEN.getMessage());
	}

	@Nested
	class CanParseTest {
		@Test
		@DisplayName("유효한 토큰을 파싱을 시도하면 true를 리턴해야 한다.")
		void isParsable_Success_ByValidToken() {
			// given

			// when
			boolean shouldBeTrue = jwtParser.isParsable(accessToken);

			// then
			assertThat(shouldBeTrue).isTrue();
		}

		@Test
		@DisplayName("유효하지 않은 토큰으로 파싱을 시도하면 false를 리턴해야 한다.")
		void isParsable_Fail_ByInvalidToken() {
			// given
			String invalidToken = """
				eyJhbGciOiJIUzI1NiJ9.
				eyJuYW1lIjoiSGVqb3cifQ.
				SI7XBRHE_95nkxQ69SiiCQcqDkZ-FW1RdxNL1DmAAAg
				""";

			// when
			boolean shouldBeFalse = jwtParser.isParsable(invalidToken);

			// then
			assertThat(shouldBeFalse).isFalse();
		}
	}
}
