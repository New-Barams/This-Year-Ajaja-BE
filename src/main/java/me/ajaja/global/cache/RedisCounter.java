package me.ajaja.global.cache;

import static me.ajaja.global.exception.ErrorCode.*;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.remind.application.SendTrialCounter;

@Component
@RequiredArgsConstructor
class RedisCounter implements SendTrialCounter {
	private static final int INITIAL_COUNT = 1;
	private static final int MAX_TRY = 3;

	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public void count(String contact) {
		var count = (Integer)redisTemplate.opsForValue().get(contact);

		if (count == null) {
			redisTemplate.opsForValue().set(contact, INITIAL_COUNT, Duration.ofDays(1));
			return;
		}

		if (count >= MAX_TRY) {
			throw new AjajaException(OVER_FREE_TRIAL);
		}
	}
}
