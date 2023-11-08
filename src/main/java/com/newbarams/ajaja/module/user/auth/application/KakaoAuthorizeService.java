package com.newbarams.ajaja.module.user.auth.application;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.infra.feign.kakao.KakaoAuthorizeFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.KakaoProperties;
import com.newbarams.ajaja.module.user.application.AuthorizeService;
import com.newbarams.ajaja.module.user.auth.model.AccessToken;
import com.newbarams.ajaja.module.user.auth.model.KakaoRequest;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
class KakaoAuthorizeService implements AuthorizeService {
	private static final String KAKAO_GRANT_TYPE = "authorization_code";

	private final KakaoAuthorizeFeignClient kakaoAuthorizeFeignClient;
	private final KakaoProperties kakaoProperties;

	@Override
	public AccessToken authorize(String authorizationCode) {
		Objects.requireNonNull(authorizationCode, "authorizationCode must not be null");
		KakaoRequest.Token request = generateRequest(authorizationCode);
		return kakaoAuthorizeFeignClient.authorize(request);
	}

	private KakaoRequest.Token generateRequest(String authorizationCode) {
		return new KakaoRequest.Token(
			KAKAO_GRANT_TYPE,
			kakaoProperties.getClientId(),
			kakaoProperties.getRedirectUri(),
			authorizationCode,
			kakaoProperties.getClientSecret()
		);
	}
}
