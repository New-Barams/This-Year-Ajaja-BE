package com.newbarams.ajaja.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.newbarams.ajaja.infra.feign.common.FeignErrorDecoder;

import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;

@Configuration
@EnableFeignClients("com.newbarams.ajaja.infra.feign.kakao")
public class OpenFeignConfig {

	@Bean
	ErrorDecoder errorDecoder() {
		return new FeignErrorDecoder();
	}

	@Bean
	Logger.Level loggerLevel() {
		return Logger.Level.BASIC;
	}

	/**
	 * Retry 3 times, Frequency increases 1s to 2s
	 */
	@Bean
	Retryer retryer() {
		return new Retryer.Default(1000L, 2000L, 3);
	}
}
