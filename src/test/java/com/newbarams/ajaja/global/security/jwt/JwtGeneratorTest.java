package com.newbarams.ajaja.global.security.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;

import com.newbarams.ajaja.common.annotation.RedisBasedTest;
import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.module.auth.dto.AuthResponse;

@RedisBasedTest
class JwtGeneratorTest extends MockTestSupport {
	@Autowired
	private JwtGenerator jwtGenerator;
	@Autowired
	private JwtSecretProvider jwtSecretProvider;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@SpyBean
	private JwtParser jwtParser;

	private Long userId;

	@BeforeEach
	void setup() {
		userId = sut.giveMeOne(Long.class);
	}

	@AfterEach
	void clean() {
		redisTemplate.delete(jwtSecretProvider.getSignature() + userId);
	}

	@Test
	@DisplayName("로그인 시 두 종류의 JWT를 생성하고 그 중에서 Refresh Token은 cache에 저장되어야 한다.")
	void login_Success_WithRefreshTokenSavedOnRedis() {
		// given

		// when
		AuthResponse.Token response = jwtGenerator.login(userId);

		// then
		assertThat(response.getAccessToken()).isNotNull();
		assertThat(response.getRefreshToken()).isNotNull();

		Object savedToken = redisTemplate.opsForValue().get(jwtSecretProvider.getSignature() + userId);
		assertThat(savedToken).isNotNull();
		assertThat(savedToken).isInstanceOf(String.class);
		assertThat(response.getRefreshToken()).isEqualTo((String)savedToken);
		assertThat(response.getAccessTokenExpireIn()).isGreaterThan(new Date().getTime());
	}

	// @Test
	// @DisplayName("재발급 시 Refresh Token의 만료일이 3일 이상 남았다면 Access Token만 재발급되어야 한다.")
	// void reissue_Success_WithOnlyAccessTokenGenerated() throws InterruptedException {
	// 	// given
	// 	AuthResponse.Token logined = jwtGenerator.login(userId);
	// 	TimeUnit.SECONDS.sleep(1); // to avoid test fail
	//
	// 	// when
	// 	AuthResponse.Token reissued = jwtGenerator.reissue(userId, logined.getRefreshToken());
	//
	// 	// then
	// 	assertThat(reissued.getAccessToken()).isNotEqualTo(logined.getAccessToken());
	// 	assertThat(reissued.getRefreshToken()).isEqualTo(logined.getRefreshToken());
	//
	// 	Object savedToken = redisTemplate.opsForValue().get(jwtSecretProvider.getSignature() + userId);
	// 	assertThat(savedToken).isNotNull();
	// 	assertThat(savedToken).isInstanceOf(String.class);
	// 	assertThat(reissued.getRefreshToken()).isEqualTo((String)savedToken);
	// }

	@Test
	@DisplayName("재발급 시 Refresh Token의 만료일이 3일 이하라면 모두 새롭게 발급되어야 한다.")
	void reissue_Success_WithNewRefreshToken() throws InterruptedException {
		// given
		AuthResponse.Token logined = jwtGenerator.login(userId);

		Date now = new Date();
		Date twoDaysBefore = new Date(now.getTime() - 60 * 60 * 24 * 2 * 1000L);
		given(jwtParser.parseExpireIn(logined.getRefreshToken())).willReturn(twoDaysBefore);
		TimeUnit.SECONDS.sleep(1); // to avoid test fail

		// when
		AuthResponse.Token reissued = jwtGenerator.reissue(userId, logined.getRefreshToken());

		// then
		assertThat(reissued.getAccessToken()).isNotEqualTo(logined.getAccessToken());
		assertThat(reissued.getRefreshToken()).isNotEqualTo(logined.getRefreshToken());

		Object savedToken = redisTemplate.opsForValue().get(jwtSecretProvider.getSignature() + userId);
		assertThat(savedToken).isNotNull();
		assertThat(savedToken).isInstanceOf(String.class);
		assertThat(reissued.getRefreshToken()).isEqualTo((String)savedToken);
	}
}
