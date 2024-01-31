package com.newbarams.ajaja.module.auth.adapter.out.kakao;

import static com.newbarams.ajaja.global.util.BearerUtil.*;

import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.client.KakaoAuthorizeFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.client.KakaoProfileFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.client.KakaoProperties;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoRequest;
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
		KakaoRequest.Token request = generateRequest(authorizationCode, redirectUri);
		AccessToken accessToken = kakaoAuthorizeFeignClient.authorize(request);
		return kakaoProfileFeignClient.getKakaoProfile(toBearer(accessToken.getContent()));
	}

	private KakaoRequest.Token generateRequest(String authorizationCode, String redirectUri) {
		return new KakaoRequest.Token(
			kakaoProperties.getClientId(),
			redirectUri,
			authorizationCode,
			kakaoProperties.getClientSecret()
		);
	}
}
