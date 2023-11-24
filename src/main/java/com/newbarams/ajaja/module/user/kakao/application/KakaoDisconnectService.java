package com.newbarams.ajaja.module.user.kakao.application;

import static com.newbarams.ajaja.global.util.BearerTokenUtil.*;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.KakaoUnlinkFeignClient;
import com.newbarams.ajaja.module.user.application.DisconnectService;
import com.newbarams.ajaja.module.user.application.model.AccessToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class KakaoDisconnectService implements DisconnectService {
	private final KakaoAuthorizeService kakaoAuthorizeService;
	private final KakaoUnlinkFeignClient kakaoUnlinkFeignClient;

	@Async
	@Override
	public void disconnect(String authorizationCode, String redirectUri) {
		AccessToken accessToken = kakaoAuthorizeService.authorize(authorizationCode, redirectUri);
		kakaoUnlinkFeignClient.unlink(toBearer(accessToken.getContent()));
	}
}
