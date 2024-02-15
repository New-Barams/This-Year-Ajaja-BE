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
	private final KakaoUnlinkFeignClient kakaoUnlinkFeignClient;
	private final KakaoProperties kakaoProperties;

	@Async
	@Override
	public void disconnect(Long oauthId) {
		KakaoRequest.Unlink request = new KakaoRequest.Unlink(oauthId);
		KakaoResponse.Withdraw result = kakaoUnlinkFeignClient.unlink(kakaoProperties.asHeader(), request);

		if (Objects.equals(result.getId(), oauthId)) {
			log.info("[Kakao Oauth Disconnected] disconnected oauth ID : {}", oauthId);
		}
	}
}
