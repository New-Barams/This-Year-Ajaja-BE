package me.ajaja.module.user.adapter.out.kakao;

import java.util.Objects;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ajaja.infra.feign.kakao.client.KakaoProperties;
import me.ajaja.infra.feign.kakao.client.KakaoUnlinkFeignClient;
import me.ajaja.infra.feign.kakao.model.KakaoRequest;
import me.ajaja.infra.feign.kakao.model.KakaoResponse;
import me.ajaja.module.user.application.port.out.DisconnectOauthPort;

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
