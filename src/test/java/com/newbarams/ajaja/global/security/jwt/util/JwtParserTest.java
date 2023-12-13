package com.newbarams.ajaja.global.security.jwt.util;

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

import com.newbarams.ajaja.common.annotation.RedisBasedTest;
import com.newbarams.ajaja.common.support.MonkeySupport;
import com.newbarams.ajaja.global.security.common.CustomUserDetailService;
import com.newbarams.ajaja.global.security.common.UserAdapter;
import com.newbarams.ajaja.module.user.dto.UserResponse;

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
	private CustomUserDetailService customUserDetailService;

	private Long userId;
	private String accessToken;

	@BeforeEach
	void setup() {
		userId = sut.giveMeOne(Long.class);
		UserResponse.Token response = jwtGenerator.generate(userId);
		accessToken = response.getAccessToken();
	}

	@AfterEach
	void clearCache() {
		redisTemplate.delete(jwtSecretProvider.getSignature() + userId);
	}

	@Test
	@DisplayName("JWT에서 ID를 파싱하면 생성했을 때의 ID와 동일하고 예외가 발생하지 않아야 한다.")
	void parseId_Success_WithSameUserId() {
		// given

		// when, then
		assertThatNoException().isThrownBy(() -> {
			Long parsedId = jwtParser.parseId(accessToken);
			assertThat(userId).isEqualTo(parsedId);
		});
	}

	@Test
	@DisplayName("JWT에서 Authentication를 파싱하면 UsernamePasswordAuthenticationToken으로 생성되어야 한다.")
	void parseAuthentication_Success_WithSameUserAdapter() {
		// given
		UserAdapter adapter = sut.giveMeOne(UserAdapter.class);
		given(customUserDetailService.loadUserByUsername(anyString())).willReturn(adapter);

		// when
		Authentication authentication = jwtParser.parseAuthentication(accessToken);

		// then
		then(customUserDetailService).should(times(1)).loadUserByUsername(anyString());
		assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken.class);
		assertThat(authentication.getPrincipal()).isEqualTo(adapter);
	}
}
