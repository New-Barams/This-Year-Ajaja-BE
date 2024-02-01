package me.ajaja.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import me.ajaja.infra.feign.common.FeignErrorDecoder;
import me.ajaja.infra.feign.common.FeignResponseEncoder;

@Configuration
@EnableFeignClients("me.ajaja.infra.feign")
public class OpenFeignConfig {

	@Bean
	ErrorDecoder errorDecoder() {
		return new FeignErrorDecoder(new FeignResponseEncoder());
	}

	@Bean
	Logger.Level loggerLevel() {
		return Logger.Level.FULL;
	}

	/**
	 * Retry 3 times, Frequency increases 1s to 2s
	 */
	@Bean
	Retryer retryer() {
		return new Retryer.Default(1000L, 2000L, 3);
	}
}
