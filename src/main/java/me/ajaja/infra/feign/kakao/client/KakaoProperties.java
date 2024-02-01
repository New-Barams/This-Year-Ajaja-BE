package me.ajaja.infra.feign.kakao.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "secret.kakao")
public class KakaoProperties {
	private final String adminKey;
	private final String clientId;
	private final String clientSecret;
	private final String logoutRedirectUrl;
}
