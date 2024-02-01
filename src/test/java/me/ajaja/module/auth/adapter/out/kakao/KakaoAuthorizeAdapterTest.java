package me.ajaja.module.auth.adapter.out.kakao;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.ajaja.common.support.MockTestSupport;
import me.ajaja.infra.feign.kakao.client.KakaoAuthorizeFeignClient;
import me.ajaja.infra.feign.kakao.client.KakaoProfileFeignClient;
import me.ajaja.infra.feign.kakao.client.KakaoProperties;
import me.ajaja.infra.feign.kakao.model.KakaoResponse;
import me.ajaja.module.auth.application.model.Profile;

class KakaoAuthorizeAdapterTest extends MockTestSupport {
	@InjectMocks
	private KakaoAuthorizeAdapter kakaoAuthorizeAdapter;

	@Mock
	private KakaoAuthorizeFeignClient kakaoAuthorizeFeignClient;
	@Mock
	private KakaoProperties kakaoProperties;
	@Mock
	private KakaoProfileFeignClient kakaoProfileFeignClient;

	@Test
	@DisplayName("인증을 요청하면 예상한 정보와 일치해야 한다.")
	void authorize_Success_WithoutException() {
		// given
		String authorizationCode = sut.giveMeOne(String.class);
		String redirectUrl = sut.giveMeOne(String.class);
		KakaoResponse.Token token = sut.giveMeOne(KakaoResponse.Token.class);
		KakaoResponse.UserInfo userInfo = sut.giveMeOne(KakaoResponse.UserInfo.class);

		given(kakaoAuthorizeFeignClient.authorize(any())).willReturn(token);
		given(kakaoProfileFeignClient.getKakaoProfile(any())).willReturn(userInfo);

		// when
		Profile profile = kakaoAuthorizeAdapter.authorize(authorizationCode, redirectUrl);

		// then
		then(kakaoAuthorizeFeignClient).should(times(1)).authorize(any());
		then(kakaoProfileFeignClient).should(times(1)).getKakaoProfile(any());
		assertThat(profile.getOauthId()).isEqualTo(userInfo.getId());
		assertThat(profile.getEmail()).isEqualTo(userInfo.getKakaoAccount().email());
	}
}
