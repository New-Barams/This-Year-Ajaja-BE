package com.newbarams.ajaja.module.auth.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.common.support.MonkeySupport;
import com.newbarams.ajaja.global.security.jwt.JwtGenerator;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoAccount;
import com.newbarams.ajaja.infra.feign.kakao.model.KakaoResponse;
import com.newbarams.ajaja.module.auth.application.model.Profile;
import com.newbarams.ajaja.module.auth.application.port.out.AuthorizePort;
import com.newbarams.ajaja.module.user.adapter.out.persistence.UserJpaRepository;
import com.newbarams.ajaja.module.user.adapter.out.persistence.model.UserEntity;
import com.newbarams.ajaja.module.user.application.port.out.ApplyChangePort;
import com.newbarams.ajaja.module.user.application.port.out.CreateUserPort;
import com.newbarams.ajaja.module.user.application.port.out.RetrieveUserPort;
import com.newbarams.ajaja.module.user.domain.Email;
import com.newbarams.ajaja.module.user.domain.PhoneNumber;
import com.newbarams.ajaja.module.user.domain.User;

@Transactional
@SpringBootTest
class LoginServiceTest extends MonkeySupport {
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserJpaRepository userRepository;

	@SpyBean
	private RetrieveUserPort retrieveUserPort;
	@SpyBean
	private CreateUserPort createUserPort;
	@SpyBean
	private ApplyChangePort applyChangePort;

	@MockBean
	private AuthorizePort authorizePort;
	@MockBean
	private JwtGenerator jwtGenerator;

	// login parameters
	private final String authorizationCode = sut.giveMeOne(String.class);
	private final String redirectUrl = sut.giveMeOne(String.class);

	// returns
	private final String email = "Ajaja@me.com";
	private final KakaoAccount kakaoAccount = sut.giveMeBuilder(KakaoAccount.class)
		.set("phoneNumber", "+82 10-1234-5678")
		.set("email", email)
		.sample();
	private final Profile profile = sut.giveMeBuilder(KakaoResponse.UserInfo.class)
		.set("kakaoAccount", kakaoAccount)
		.sample();

	@Test
	@DisplayName("요청한 이메일로 어떤 기록도 존재하지 않으면 새로운 사용자를 생성해야 한다.")
	void login_Success_AndCreateUser() {
		// given
		User user = User.init(1L, "+82 1012345678", email);

		given(authorizePort.authorize(any(), any())).willReturn(profile);
		given(retrieveUserPort.loadByEmail(anyString())).willReturn(Optional.empty());
		given(createUserPort.create(user)).willReturn(1L);

		// when
		loginService.login(authorizationCode, redirectUrl);

		// then
		then(authorizePort).should(times(1)).authorize(any(), any());
		then(retrieveUserPort).should(times(1)).loadByEmail(anyString());
		then(createUserPort).should(times(1)).create(any());
		then(jwtGenerator).should(times(1)).login(any());
	}

	@Test
	@DisplayName("가입 이력이 존재하는 고객이 로그인하면 회원가입되지 않아야 한다.")
	void login_Success_WithoutCreateUser() {
		// given
		User user = sut.giveMeBuilder(User.class) // when using static method id will be null
			.set("phoneNumber", new PhoneNumber("01012345678"))
			.set("email", Email.init(email))
			.sample();

		given(authorizePort.authorize(any(), any())).willReturn(profile);
		given(retrieveUserPort.loadByEmail(anyString())).willReturn(Optional.of(user));

		// when
		loginService.login(authorizationCode, redirectUrl);

		// then
		then(authorizePort).should(times(1)).authorize(any(), any());
		then(retrieveUserPort).should(times(1)).loadByEmail(anyString());
		then(applyChangePort).should(times(1)).apply(any());
		then(createUserPort).shouldHaveNoMoreInteractions();
		then(jwtGenerator).should(times(1)).login(any());
	}

	@Test
	@DisplayName("가입 이력이 존재하는 사용자가 새로운 번호로 로그인하면 번호가 최신화되어야 한다.")
	void login_Success_WithUpdatedPhoneNumber() {
		// given
		String oldNumber = "01000000000";
		String expected = "01012345678";

		userRepository.save(sut.giveMeBuilder(UserEntity.class)
			.set("nickname", "nickname")
			.set("phoneNumber", oldNumber)
			.set("signUpEmail", email)
			.set("remindEmail", email)
			.set("remindType", "KAKAO")
			.set("deleted", false)
			.sample());

		given(authorizePort.authorize(any(), any())).willReturn(profile);

		// when
		loginService.login(authorizationCode, redirectUrl);

		// then
		then(authorizePort).should(times(1)).authorize(any(), any());
		then(createUserPort).shouldHaveNoMoreInteractions();

		List<UserEntity> entities = userRepository.findAll();
		assertThat(entities).isNotEmpty();

		UserEntity saved = entities.get(0);
		assertThat(saved.getPhoneNumber()).isNotEqualTo(oldNumber);
		assertThat(saved.getPhoneNumber()).isEqualTo(expected);
	}

	@Test
	@DisplayName("탈퇴한 유저가 로그인하면 새로운 계정을 만들어야 한다.")
	void login_Success_ReSignUpWithdrawUser() {
		// given
		UserEntity entity = sut.giveMeBuilder(UserEntity.class)
			.set("nickname", "nickname")
			.set("phoneNumber", "01012345678")
			.set("signUpEmail", email)
			.set("remindEmail", email)
			.set("remindType", "KAKAO")
			.set("deleted", true)
			.sample();

		userRepository.save(entity);
		given(authorizePort.authorize(any(), any())).willReturn(profile);

		// when
		loginService.login(authorizationCode, redirectUrl);

		// then
		then(authorizePort).should(times(1)).authorize(any(), any());

		List<UserEntity> entities = userRepository.findAll();
		assertThat(entities).isNotEmpty();

		UserEntity saved = entities.get(0);
		assertThat(saved).usingRecursiveComparison().isNotEqualTo(entity);
	}
}
