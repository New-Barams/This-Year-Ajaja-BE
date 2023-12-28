package com.newbarams.ajaja.module.user.kakao.application;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.infra.feign.kakao.client.KakaoProperties;
import com.newbarams.ajaja.infra.feign.kakao.client.KakaoUnlinkFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoResponse;

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
		Long oauthId = sut.giveMeOne(Long.class);
		String adminKey = sut.giveMeOne(String.class);
		KakaoResponse.Withdraw response = sut.giveMeOne(KakaoResponse.Withdraw.class);

		given(kakaoProperties.getAdminKey()).willReturn(adminKey);
		given(kakaoUnlinkFeignClient.unlink(anyString(), any())).willReturn(response);

		// when
		kakaoDisconnectService.disconnect(oauthId);

		// then
		then(kakaoUnlinkFeignClient).should(times(1)).unlink(anyString(), any());
	}
}
