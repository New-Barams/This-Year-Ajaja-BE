package me.ajaja.global.security.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;

import me.ajaja.common.annotation.RedisBasedTest;
import me.ajaja.common.support.MockTestSupport;
import me.ajaja.module.auth.dto.AuthResponse;

@RedisBasedTest
class JwtGeneratorTest extends MockTestSupport {
	private static final long ONE_DAY = 60 * 60 * 24 * 1000L;

	@Autowired
	private JwtGenerator jwtGenerator;
	@Autowired
	private JwtSecretProvider jwtSecretProvider;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@MockBean
	private RawParser rawParser;

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

	@Test
	@DisplayName("Refresh Token의 만료일이 3일 이상 남았다면 Access Token만 재발급되어야 한다.")
	void reissue_Success_WithOnlyAccessTokenGenerated() throws InterruptedException {
		// given
		AuthResponse.Token tokens = jwtGenerator.login(userId);

		Date now = new Date();
		Date expiredAt = new Date(now.getTime() - ONE_DAY * 4); // expiration 4 days left

		given(rawParser.parseClaimWithType(anyString(), anyString(), any())).willReturn(expiredAt);
		TimeUnit.SECONDS.sleep(1); // to avoid test fail

		// when
		AuthResponse.Token reissued = jwtGenerator.reissue(userId, tokens.getRefreshToken());

		// then
		assertThat(reissued.getAccessToken()).isNotEqualTo(tokens.getAccessToken());
		assertThat(reissued.getRefreshToken()).isEqualTo(tokens.getRefreshToken());

		Object savedToken = redisTemplate.opsForValue().get(jwtSecretProvider.getSignature() + userId);
		assertThat(savedToken).isNotNull();
		assertThat(savedToken).isInstanceOf(String.class);
		assertThat(reissued.getRefreshToken()).isEqualTo((String)savedToken);
	}

	@Test
	@DisplayName("Refresh Token의 만료일이 3일 이하라면 모두 재발급되어야 한다")
	void reissue_Success_WithNewRefreshToken() throws InterruptedException {
		// given
		AuthResponse.Token tokens = jwtGenerator.login(userId);

		Date now = new Date();
		Date expiredAt = new Date(now.getTime() - ONE_DAY * 2); // expiration 2 days left

		given(rawParser.parseClaimWithType(anyString(), anyString(), any())).willReturn(expiredAt);
		TimeUnit.SECONDS.sleep(1); // to avoid test fail

		// when
		AuthResponse.Token reissued = jwtGenerator.reissue(userId, tokens.getRefreshToken());

		// then
		assertThat(reissued.getAccessToken()).isNotEqualTo(tokens.getAccessToken());
		assertThat(reissued.getRefreshToken()).isNotEqualTo(tokens.getRefreshToken());

		Object savedToken = redisTemplate.opsForValue().get(jwtSecretProvider.getSignature() + userId);
		assertThat(savedToken).isNotNull();
		assertThat(savedToken).isInstanceOf(String.class);
		assertThat(reissued.getRefreshToken()).isEqualTo((String)savedToken);
	}
}
