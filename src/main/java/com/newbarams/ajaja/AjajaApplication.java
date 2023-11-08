package com.newbarams.ajaja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

import com.newbarams.ajaja.infra.feign.kakao.KakaoProperties;
@EnableConfigurationProperties(KakaoProperties.class)
@SpringBootApplication
public class AjajaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AjajaApplication.class, args);
	}
}
