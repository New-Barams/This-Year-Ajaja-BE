package me.ajaja.global.security.jwt;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import me.ajaja.common.annotation.RedisBasedTest;
import me.ajaja.common.support.MonkeySupport;

@RedisBasedTest
class JwtExpirerTest extends MonkeySupport {
	@Autowired
	private JwtExpirer jwtExpirer;
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
		jwtGenerator.login(userId);

		// when
		jwtExpirer.expire(userId);

		// then
		assertThat(redisTemplate.opsForValue().get(jwtSecretProvider.getSignature() + userId)).isNull();
	}
}
