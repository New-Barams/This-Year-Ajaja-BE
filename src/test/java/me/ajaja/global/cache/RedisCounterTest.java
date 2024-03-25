package me.ajaja.global.cache;

import static me.ajaja.common.extenstion.AssertExtension.*;
import static me.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import me.ajaja.common.annotation.RedisBasedTest;

@RedisBasedTest
class RedisCounterTest {
	@Autowired
	private RedisCounter counter;
	@Autowired
	private RedisTemplate<String, Object> redis;

	private final String contact = "01012341234";

	@AfterEach
	void clear() {
		redis.delete(contact);
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 3})
	@DisplayName("카운트를 요청하면 요청한 회수만큼 등록되어야 한다.")
	void count_Success_OnFirst(int tries) {
		// given

		// when
		for (int i = 0; i < tries; i++) {
			counter.count(contact);
		}

		// then
		assertAll(
			() -> assertThat(redis.opsForValue().get(contact)).isNotNull(),
			() -> assertThat((Integer)redis.opsForValue().get(contact)).isEqualTo(tries)
		);
	}

	@Test
	@DisplayName("최대 시도 회수를 넘어가면 예외가 발생한다.")
	void count_Fail_ByOverMax() {
		// given
		for (int i = 0; i < 3; i++) {
			counter.count(contact);
		}

		// when, then
		assertThatAjajaException(OVER_FREE_TRIAL)
			.isThrownBy(() -> counter.count(contact));
	}
}
