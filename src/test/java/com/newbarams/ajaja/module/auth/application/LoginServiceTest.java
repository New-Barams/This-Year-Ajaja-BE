package com.newbarams.ajaja.module.auth.application;

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
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoAccount;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoResponse;
import com.newbarams.ajaja.module.auth.application.model.Profile;
import com.newbarams.ajaja.module.auth.application.port.out.AuthorizePort;
import com.newbarams.ajaja.module.user.application.port.out.CreateUserPort;
import com.newbarams.ajaja.module.user.application.port.out.FindUserIdByEmailPort;

class LoginServiceTest extends MockTestSupport {
	@InjectMocks
	private LoginService loginService;

	@Mock
	private AuthorizePort authorizePort;
	@Mock
	private FindUserIdByEmailPort findUserIdByEmailPort;
	@Mock
	private CreateUserPort createUserPort;
	@Mock
	private JwtGenerator jwtGenerator;

	@Nested
	@DisplayName("로그인 테스트")
	class LoginTest {
		// login parameters
		private final String authorizationCode = sut.giveMeOne(String.class);
		private final String redirectUrl = sut.giveMeOne(String.class);

		// returns
		private final String email = "Ajaja@me.com";
		private final Profile profile = sut.giveMeBuilder(KakaoResponse.UserInfo.class)
			.set("kakaoAccount", sut.giveMeBuilder(KakaoAccount.class)
				.set("email", email)
				.sample())
			.sample();

		@Test
		@DisplayName("새로운 유저가 로그인하면 새롭게 유저 정보를 생성해야 한다.")
		void login_Success_WithNewUser() {
			// given
			given(authorizePort.authorize(any(), any())).willReturn(profile);
			given(findUserIdByEmailPort.findUserIdByEmail(any())).willReturn(Optional.empty());
			given(createUserPort.create(anyString(), any())).willReturn(1L);

			// when
			loginService.login(authorizationCode, redirectUrl);

			// then
			then(authorizePort).should(times(1)).authorize(any(), any());
			then(findUserIdByEmailPort).should(times(1)).findUserIdByEmail(any());
			then(createUserPort).should(times(1)).create(anyString(), any());
			then(jwtGenerator).should(times(1)).login(any());
		}

		@Test
		@DisplayName("기존에 가입된 고객이 로그인하면 생성하는 로직이 호출되지 않아야 한다.")
		void login_Success_WithOldUser() {
			// given
			given(authorizePort.authorize(any(), any())).willReturn(profile);
			given(findUserIdByEmailPort.findUserIdByEmail(any())).willReturn(Optional.of(1L));

			// when
			loginService.login(authorizationCode, redirectUrl);

			// then
			then(authorizePort).should(times(1)).authorize(any(), any());
			then(findUserIdByEmailPort).should(times(1)).findUserIdByEmail(any());
			then(createUserPort).shouldHaveNoMoreInteractions();
			then(jwtGenerator).should(times(1)).login(any());
		}
	}
}
