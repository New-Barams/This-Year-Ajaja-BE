package com.newbarams.ajaja.module.user.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.MockTestSupport;
import com.newbarams.ajaja.infra.feign.kakao.KakaoProfileFeignClient;
import com.newbarams.ajaja.module.user.auth.model.KakaoResponse;
import com.newbarams.ajaja.module.user.auth.model.Profile;

class KakaoGetProfileServiceTest extends MockTestSupport {
	@InjectMocks
	private KakaoGetProfileService kakaoGetProfileService;

	@Mock
	private KakaoProfileFeignClient kakaoProfileFeignClient;

	@Test
	@DisplayName("프로필 정보를 요청하면 예상한 이메일과 일치하고 예외가 발생하지 않아야 한다.")
	void getProfile_Success_WithoutException() {
		// given
		String accessToken = monkey.giveMeOne(String.class);
		KakaoResponse.UserInfo kakaoProfile = monkey.giveMeOne(KakaoResponse.UserInfo.class);
		given(kakaoProfileFeignClient.getKakaoProfile(any())).willReturn(kakaoProfile);

		// when
		Profile profile = kakaoGetProfileService.getProfile(accessToken);

		// then
		then(kakaoProfileFeignClient).should(times(1)).getKakaoProfile(any());
		assertThat(kakaoProfile.kakaoAccount().email()).isEqualTo(profile.getEmail());
	}

	@Test
	@DisplayName("null이 입력되면 NPE를 던진다.")
	void getProfile_Fail_ByNullAccessToken() {
		// given
		String accessToken = null;

		// when, then
		assertThatThrownBy(() -> kakaoGetProfileService.getProfile(accessToken))
			.isInstanceOf(NullPointerException.class);
		then(kakaoProfileFeignClient).shouldHaveNoInteractions();
	}
}
