package com.newbarams.ajaja.module.user.kakao.application;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.infra.feign.kakao.KakaoProperties;
import com.newbarams.ajaja.infra.feign.kakao.KakaoUnlinkFeignClient;

class KakaoDisconnectOauthServiceTest extends MockTestSupport {
	@InjectMocks
	private KakaoDisconnectOauthService kakaoDisconnectService;

	@Mock
	private KakaoUnlinkFeignClient kakaoUnlinkFeignClient;

	@Mock
	private KakaoProperties kakaoProperties;

	@Test
	void disconnect_Success() {
		// given
		Long oauthId = monkey.giveMeOne(Long.class);
		String adminKey = monkey.giveMeOne(String.class);
		given(kakaoProperties.getAdminKey()).willReturn(adminKey);

		// when
		kakaoDisconnectService.disconnect(oauthId);

		// then
		then(kakaoUnlinkFeignClient).should(times(1)).unlink(anyString(), any());
	}
}
