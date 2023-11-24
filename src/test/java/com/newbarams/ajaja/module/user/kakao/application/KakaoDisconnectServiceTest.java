package com.newbarams.ajaja.module.user.kakao.application;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.infra.feign.kakao.KakaoUnlinkFeignClient;
import com.newbarams.ajaja.module.user.application.model.AccessToken;
import com.newbarams.ajaja.module.user.kakao.model.KakaoResponse;

class KakaoDisconnectServiceTest extends MockTestSupport {
	@InjectMocks
	private KakaoDisconnectService kakaoDisconnectService;

	@Mock
	private KakaoAuthorizeService kakaoAuthorizeService;

	@Mock
	private KakaoUnlinkFeignClient kakaoUnlinkFeignClient;

	@Test
	void disconnect_Success() {
		// given
		String authorizationCode = monkey.giveMeOne(String.class);
		String redirectUri = monkey.giveMeOne(String.class);
		AccessToken accessToken = monkey.giveMeOne(KakaoResponse.Token.class);

		given(kakaoAuthorizeService.authorize(anyString(), anyString())).willReturn(accessToken);

		// when
		kakaoDisconnectService.disconnect(authorizationCode, redirectUri);

		// then
		then(kakaoAuthorizeService).should(times(1)).authorize(anyString(), anyString());
		then(kakaoUnlinkFeignClient).should(times(1)).unlink(anyString());
	}
}
