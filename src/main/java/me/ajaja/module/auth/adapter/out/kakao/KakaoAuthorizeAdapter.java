package me.ajaja.module.auth.adapter.out.kakao;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.util.BearerUtil;
import me.ajaja.infra.feign.kakao.client.KakaoAuthorizeFeignClient;
import me.ajaja.infra.feign.kakao.client.KakaoProfileFeignClient;
import me.ajaja.infra.feign.kakao.client.KakaoProperties;
import me.ajaja.infra.feign.kakao.model.KakaoRequest;
import me.ajaja.module.auth.application.model.AccessToken;
import me.ajaja.module.auth.application.model.Profile;
import me.ajaja.module.auth.application.port.out.AuthorizePort;

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
		return kakaoProfileFeignClient.getKakaoProfile(BearerUtil.toBearer(accessToken.getContent()));
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
