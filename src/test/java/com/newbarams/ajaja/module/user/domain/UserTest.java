package com.newbarams.ajaja.module.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {
	private final String nickname = "imhejow";

	@ParameterizedTest
	@ValueSource(strings = {"gmlwh124@naver.com", "ajaja.net@gamil.com", "123123@kakao.net"})
	@DisplayName("유효한 이메일로 유저를 생성하면 예외가 발생하지 않는다.")
	void create_Success_WithoutExceptionAndNormalState(String input) {
		// given

		// when, then
		assertThatNoException().isThrownBy(() -> new User(nickname, input));
	}

	@Test
	@DisplayName("미검증 유저의 이메일을 검증하면 예외가 발생하지 않아야 한다.")
	void verifyEmail_Success_WithoutException() {
		// given
		String email = "gmlwh124@naver.com";
		User user = new User(nickname, email);

		// when, then
		assertThatNoException().isThrownBy(user::verifyEmail);
	}

	@Test
	@DisplayName("이미 검증을 실시한 유저는 예외를 던져야 한다.")
	void verifyEmail_Fail_ByAlreadyVerified() {
		// given
		String email = "gmlwh124@naver.com";
		User user = new User(nickname, email);
		user.verifyEmail();

		// when, then
		assertThatException().isThrownBy(user::verifyEmail);
	}

	@Test
	void getEmail_Success() {
		// given
		String email = "gmlwh124@naver.com";
		User user = new User(nickname, email);

		// when, then
		assertThat(user.defaultEmail()).isEqualTo(email);
	}
}
