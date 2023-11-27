package com.newbarams.ajaja.module.user.kakao.application;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.KakaoProperties;
import com.newbarams.ajaja.infra.feign.kakao.KakaoUnlinkFeignClient;
import com.newbarams.ajaja.module.user.application.DisconnectOauthService;
import com.newbarams.ajaja.module.user.kakao.model.KakaoUnlinkRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class KakaoDisconnectOauthService implements DisconnectOauthService {
	private static final String KAKAO_AK_PREFIX = "KakaoAK ";

	private final KakaoUnlinkFeignClient kakaoUnlinkFeignClient;
	private final KakaoProperties kakaoProperties;

	@Async
	@Override
	public void disconnect(Long oauthId) {
		kakaoUnlinkFeignClient.unlink(kakaoHeader(), new KakaoUnlinkRequest(oauthId));
	}

	private String kakaoHeader() {
		return KAKAO_AK_PREFIX + kakaoProperties.getAdminKey();
	}
}
