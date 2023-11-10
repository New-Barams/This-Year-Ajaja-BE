package com.newbarams.ajaja.common;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.embedded.RedisServer;

@Slf4j
@RequiredArgsConstructor
@TestConfiguration
public class TestRedisConfig {
	private final RedisProperties properties;

	private RedisServer redisServer;

	@PostConstruct
	public void redisServer() {
		redisServer = new RedisServer(properties.getPort());
		redisServer.start();
	}

	@PreDestroy
	public void stopRedis() {
		if (redisServer != null) {
			redisServer.stop();
		}
	}
}
