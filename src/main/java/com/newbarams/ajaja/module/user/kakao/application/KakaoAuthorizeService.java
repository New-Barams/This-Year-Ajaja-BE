package com.newbarams.ajaja.module.user.kakao.application;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.KakaoAuthorizeFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.KakaoProperties;
import com.newbarams.ajaja.module.user.application.model.AccessToken;
import com.newbarams.ajaja.module.user.application.port.out.AuthorizePort;
import com.newbarams.ajaja.module.user.kakao.model.KakaoTokenRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class KakaoAuthorizeService implements AuthorizePort {
	private final KakaoAuthorizeFeignClient kakaoAuthorizeFeignClient;
	private final KakaoProperties kakaoProperties;

	@Override
	public AccessToken authorize(String authorizationCode, String redirectUri) {
		KakaoTokenRequest request = generateRequest(authorizationCode, redirectUri);
		return kakaoAuthorizeFeignClient.authorize(request);
	}

	private KakaoTokenRequest generateRequest(String authorizationCode, String redirectUri) {
		return new KakaoTokenRequest(
				kakaoProperties.getClientId(),
				redirectUri,
				authorizationCode,
				kakaoProperties.getClientSecret()
		);
	}
}
