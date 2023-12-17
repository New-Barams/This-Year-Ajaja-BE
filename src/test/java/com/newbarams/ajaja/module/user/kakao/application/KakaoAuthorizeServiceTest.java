package com.newbarams.ajaja.module.user.kakao.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.infra.feign.kakao.KakaoAuthorizeFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.KakaoProperties;
import com.newbarams.ajaja.module.user.application.model.AccessToken;
import com.newbarams.ajaja.module.user.kakao.model.KakaoResponse;

class KakaoAuthorizeServiceTest extends MockTestSupport {
	@InjectMocks
	private KakaoAuthorizeService kakaoAuthorizeService;

	@Mock
	private KakaoAuthorizeFeignClient kakaoAuthorizeFeignClient;

	@Mock
	private KakaoProperties kakaoProperties;

	@Test
	@DisplayName("인증을 요청하면 예상한 정보와 일치하고 예외가 발생하지 않아야 한다.")
	void authorize_Success_WithoutException() {
		// given
		String authorizationCode = sut.giveMeOne(String.class);
		String redirectUrl = sut.giveMeOne(String.class);
		KakaoResponse.Token response = sut.giveMeOne(KakaoResponse.Token.class);
		given(kakaoAuthorizeFeignClient.authorize(any())).willReturn(response);

		// when
		AccessToken accessToken = kakaoAuthorizeService.authorize(authorizationCode, redirectUrl);

		// then
		then(kakaoAuthorizeFeignClient).should(times(1)).authorize(any());
		assertThat(accessToken.getContent()).isEqualTo(response.getAccessToken());
	}
}
