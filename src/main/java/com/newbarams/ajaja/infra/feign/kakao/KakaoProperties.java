package com.newbarams.ajaja.infra.feign.kakao;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "secret.kakao")
public class KakaoProperties {
	private final String clientId;
	private final String redirectUri;
	private final String clientSecret;
}
