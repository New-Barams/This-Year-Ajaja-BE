package com.newbarams.ajaja.module.user.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.infra.feign.kakao.KakaoAuthorizeFeignClient;
import com.newbarams.ajaja.infra.feign.kakao.KakaoProperties;
import com.newbarams.ajaja.module.user.auth.model.AccessToken;
import com.newbarams.ajaja.module.user.auth.model.KakaoResponse;

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
		String authorizationCode = monkey.giveMeOne(String.class);
		KakaoResponse.Token response = monkey.giveMeOne(KakaoResponse.Token.class);
		given(kakaoAuthorizeFeignClient.authorize(any())).willReturn(response);

		// when
		AccessToken accessToken = kakaoAuthorizeService.authorize(authorizationCode);

		// then
		then(kakaoAuthorizeFeignClient).should(times(1)).authorize(any());
		assertThat(accessToken.getContent()).isEqualTo(response.accessToken());
	}

	@Test
	@DisplayName("Client로부터 null이 들어오면 NPE를 던진다.")
	void authorize_Fail_ByNullInput() {
		// given
		String authorizationCode = null;

		// when, then
		assertThatException()
			.isThrownBy(() -> kakaoAuthorizeService.authorize(authorizationCode))
			.isInstanceOf(NullPointerException.class);
		then(kakaoAuthorizeFeignClient).shouldHaveNoInteractions();
	}

}
