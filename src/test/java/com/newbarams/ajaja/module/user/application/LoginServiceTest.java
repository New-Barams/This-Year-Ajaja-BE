package com.newbarams.ajaja.module.user.application;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.newbarams.ajaja.common.support.MockTestSupport;
import com.newbarams.ajaja.global.security.jwt.util.JwtGenerator;
import com.newbarams.ajaja.module.user.application.model.AccessToken;
import com.newbarams.ajaja.module.user.application.model.Profile;
import com.newbarams.ajaja.module.user.domain.User;
import com.newbarams.ajaja.module.user.domain.UserRepository;
import com.newbarams.ajaja.module.user.kakao.model.KakaoAccount;
import com.newbarams.ajaja.module.user.kakao.model.KakaoResponse;

class LoginServiceTest extends MockTestSupport {
	@InjectMocks
	private LoginService loginService;

	@Mock
	private AuthorizeService authorizeService;

	@Mock
	private GetProfileService getProfileService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private JwtGenerator jwtGenerator;

	@Nested
	@DisplayName("로그인 테스트")
	class LoginTest {
		private final String email = "Ajaja@me.com";
		private final String authorizationCode = sut.giveMeOne(String.class);
		private final String redirectUrl = sut.giveMeOne(String.class);
		private final AccessToken accessToken = sut.giveMeOne(KakaoResponse.Token.class);

		private final Profile profile = sut.giveMeBuilder(KakaoResponse.UserInfo.class)
			.set("kakaoAccount", sut.giveMeBuilder(KakaoAccount.class)
				.set("email", email)
				.sample())
			.sample();

		private final User user = User.init(email, 1L);

		@Test
		@DisplayName("새로운 유저가 로그인하면 새롭게 유저 정보를 생성해야 한다.")
		void login_Success_WithNewUser() {
			// given
			given(authorizeService.authorize(any(), any())).willReturn(accessToken);
			given(getProfileService.getProfile(any())).willReturn(profile);
			given(userRepository.findByEmail(any())).willReturn(Optional.empty());
			given(userRepository.save(any())).willReturn(user);

			// when
			loginService.login(authorizationCode, redirectUrl);

			// then
			then(authorizeService).should(times(1)).authorize(any(), any());
			then(getProfileService).should(times(1)).getProfile(any());
			then(userRepository).should(times(1)).findByEmail(any());
			then(userRepository).should(times(1)).save(any());
			then(jwtGenerator).should(times(1)).generate(any());
		}

		@Test
		@DisplayName("기존에 가입된 고객이 로그인하면 생성하는 로직이 호출되지 않아야 한다.")
		void login_Success_WithOldUser() {
			// given
			given(authorizeService.authorize(any(), any())).willReturn(accessToken);
			given(getProfileService.getProfile(any())).willReturn(profile);
			given(userRepository.findByEmail(any())).willReturn(Optional.of(user));

			// when
			loginService.login(authorizationCode, redirectUrl);

			// then
			then(authorizeService).should(times(1)).authorize(any(), any());
			then(getProfileService).should(times(1)).getProfile(any());
			then(userRepository).should(times(1)).findByEmail(any());
			then(userRepository).shouldHaveNoMoreInteractions();
			then(jwtGenerator).should(times(1)).generate(any());
		}
	}
}
