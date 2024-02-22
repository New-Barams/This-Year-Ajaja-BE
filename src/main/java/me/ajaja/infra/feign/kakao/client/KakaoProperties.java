package me.ajaja.infra.feign.kakao.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ajaja.infra.feign.kakao.model.KakaoRequest;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "secret.kakao")
public class KakaoProperties {
	private static final String KAKAO_AK_PREFIX = "KakaoAK ";

	private final String adminKey;
	private final String clientId;
	private final String clientSecret;
	private final String logoutRedirectUrl;

	public KakaoRequest.Authorize authorizeRequest(String authorizationCode, String redirectUri) {
		return new KakaoRequest.Authorize(clientId, redirectUri, authorizationCode, clientSecret);
	}

	public String asHeader() {
		return KAKAO_AK_PREFIX + adminKey;
	}
}
