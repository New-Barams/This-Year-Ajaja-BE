package com.newbarams.ajaja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.newbarams.ajaja.infra.feign.kakao.client.KakaoProperties;

@EnableCaching
@EnableScheduling
@EnableConfigurationProperties(KakaoProperties.class)
@SpringBootApplication
public class AjajaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AjajaApplication.class, args);
	}
}
