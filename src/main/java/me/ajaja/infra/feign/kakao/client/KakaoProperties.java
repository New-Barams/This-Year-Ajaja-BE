package me.ajaja.infra.feign.kakao.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ajaja.infra.feign.kakao.model.KakaoRequest;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "secret.kakao")
public class KakaoProperties {
	private final String adminKey;
	private final String clientId;
	private final String clientSecret;
	private final String logoutRedirectUrl;

	public KakaoRequest.Authorize authorizeRequest(String authorizationCode, String redirectUri) {
		return new KakaoRequest.Authorize(clientId, redirectUri, authorizationCode, clientSecret);
	}
}
