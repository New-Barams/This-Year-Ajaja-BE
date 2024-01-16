package com.newbarams.ajaja.module.user.adapter.out.kakao;

import java.util.Objects;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.newbarams.ajaja.infra.feign.kakao.client.KakaoProperties;
import com.newbarams.ajaja.infra.feign.kakao.client.KakaoUnlinkFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoRequest;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoResponse;
import com.newbarams.ajaja.module.user.application.port.out.DisconnectOauthPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
class KakaoDisconnectOauthAdapter implements DisconnectOauthPort {
	private static final String KAKAO_AK_PREFIX = "KakaoAK ";

	private final KakaoUnlinkFeignClient kakaoUnlinkFeignClient;
	private final KakaoProperties kakaoProperties;

	@Async
	@Override
	public void disconnect(Long oauthId) {
		KakaoResponse.Withdraw result = kakaoUnlinkFeignClient.unlink(kakaoHeader(), new KakaoRequest.Unlink(oauthId));

		if (Objects.equals(result.getId(), oauthId)) {
			log.info("Kakao Service Disconnected : {}", oauthId);
		}
	}

	private String kakaoHeader() {
		return KAKAO_AK_PREFIX + kakaoProperties.getAdminKey();
	}
}
