package com.newbarams.ajaja.module.user.auth.application;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.KakaoAuthorizeFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.KakaoProperties;
import com.newbarams.ajaja.module.user.application.AuthorizeService;
import com.newbarams.ajaja.module.user.auth.model.AccessToken;
import com.newbarams.ajaja.module.user.auth.model.KakaoTokenRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class KakaoAuthorizeService implements AuthorizeService {
	private final KakaoAuthorizeFeignClient kakaoAuthorizeFeignClient;
	private final KakaoProperties kakaoProperties;

	@Override
	public AccessToken authorize(String authorizationCode, String redirectUri) {
		KakaoTokenRequest request = generateRequest(authorizationCode, redirectUri);
		return kakaoAuthorizeFeignClient.authorize(request);
	}

	private KakaoTokenRequest generateRequest(String authorizationCode, String redirectUri) {
		return new KakaoTokenRequest(kakaoProperties.getClientId(), redirectUri, authorizationCode);
	}
}
