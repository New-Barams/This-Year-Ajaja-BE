package me.ajaja.module.auth.adapter.out.kakao;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.util.BearerUtils;
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
		KakaoRequest.Authorize request = kakaoProperties.authorizeRequest(authorizationCode, redirectUri);
		AccessToken accessToken = kakaoAuthorizeFeignClient.authorize(request);

		String bearerAccessToken = BearerUtils.toBearer(accessToken.getContent());
		return kakaoProfileFeignClient.getKakaoProfile(bearerAccessToken);
	}
}
