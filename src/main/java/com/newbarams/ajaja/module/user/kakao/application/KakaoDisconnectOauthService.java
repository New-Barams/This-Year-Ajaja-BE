package com.newbarams.ajaja.module.user.kakao.application;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.client.KakaoProperties;
import com.newbarams.ajaja.infra.feign.kakao.client.KakaoUnlinkFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoUnlinkRequest;
import com.newbarams.ajaja.module.user.application.port.out.DisconnectOauthPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class KakaoDisconnectOauthService implements DisconnectOauthPort {
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
