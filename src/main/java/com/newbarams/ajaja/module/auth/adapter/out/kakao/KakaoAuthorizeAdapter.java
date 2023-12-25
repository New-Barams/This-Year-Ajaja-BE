package com.newbarams.ajaja.module.auth.adapter.out.kakao;

import static com.newbarams.ajaja.global.util.BearerUtils.*;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.client.KakaoAuthorizeFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.client.KakaoProfileFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.client.KakaoProperties;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoTokenRequest;
import com.newbarams.ajaja.module.auth.application.model.AccessToken;
import com.newbarams.ajaja.module.auth.application.model.Profile;
import com.newbarams.ajaja.module.auth.application.port.out.AuthorizePort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class KakaoAuthorizeAdapter implements AuthorizePort {
	private final KakaoAuthorizeFeignClient kakaoAuthorizeFeignClient;
	private final KakaoProfileFeignClient kakaoProfileFeignClient;
	private final KakaoProperties kakaoProperties;

	@Override
	public Profile authorize(String authorizationCode, String redirectUri) {
		KakaoTokenRequest request = generateRequest(authorizationCode, redirectUri);
		AccessToken accessToken = kakaoAuthorizeFeignClient.authorize(request);
		return kakaoProfileFeignClient.getKakaoProfile(toBearer(accessToken.getContent()));
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
