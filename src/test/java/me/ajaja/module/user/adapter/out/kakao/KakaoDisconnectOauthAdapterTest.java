package me.ajaja.module.user.adapter.out.kakao;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.infra.feign.kakao.client.KakaoProperties;
import me.ajaja.infra.feign.kakao.client.KakaoUnlinkFeignClient;
import me.ajaja.infra.feign.kakao.model.KakaoResponse;

class KakaoDisconnectOauthAdapterTest extends MockTestSupport {
	@InjectMocks
	private KakaoDisconnectOauthAdapter kakaoDisconnectOauthAdapter;

	@Mock
	private KakaoUnlinkFeignClient kakaoUnlinkFeignClient;
	@Mock
	private KakaoProperties kakaoProperties;

	@Test
	void disconnect_Success() {
		// given
		KakaoResponse.Withdraw response = sut.giveMeOne(KakaoResponse.Withdraw.class);

		given(kakaoProperties.asHeader()).willReturn(sut.giveMeOne(String.class));
		given(kakaoUnlinkFeignClient.unlink(anyString(), any())).willReturn(response);

		// when
		kakaoDisconnectOauthAdapter.disconnect(sut.giveMeOne(Long.class));

		// then
		then(kakaoUnlinkFeignClient).should(times(1)).unlink(anyString(), any());
	}
}
