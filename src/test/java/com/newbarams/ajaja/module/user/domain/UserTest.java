package com.newbarams.ajaja.module.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.newbarams.ajaja.common.support.MonkeySupport;

class UserTest extends MonkeySupport {
	private static final String DEFAULT_EMAIL = "Ajaja@me.com";

	private User user;

	@BeforeEach
	void init() {
		user = sut.giveMeBuilder(User.class)
			.set("phoneNumber", new PhoneNumber("01012345678"))
			.set("email", Email.init(DEFAULT_EMAIL))
			.sample();
	}

	@ParameterizedTest
	@ValueSource(strings = {"gmlwh124@naver.com", "ajaja.net@gamil.com", "123123@kakao.net"})
	@DisplayName("유효한 이메일로 유저를 생성하면 예외가 발생하지 않는다.")
	void create_Success_WithoutExceptionAndNormalState(String input) {
		// given

		// when, then
		assertThatNoException().isThrownBy(() -> User.init(1L, "+82 1012345678", input));
	}

	@Test
	@DisplayName("같은 리마인드 이메일로 사용자가 이메일 인증을 완료하면 검증 상태만 변경된 새로운 객체를 가져야 한다.")
	void verified_Success_WithSameRemindEmail() {
		// given

		// when
		user.verified(DEFAULT_EMAIL);

		// then
		assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
		assertThat(user.getRemindEmail()).isEqualTo(DEFAULT_EMAIL);
		assertThat(user.isVerified()).isTrue();
	}

	@Test
	@DisplayName("다른 리마인드 이메일로 사용자가 이메일 인증을 완료하면 검증 상태와 리마인드 이메일이 변경된 새로운 객체를 가져야 한다.")
	void verified_Success_WithDifferentRemindEmail() {
		// given
		String newMail = "hejow124@naver.com";

		// when
		user.verified(newMail);

		// then
		assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
		assertThat(user.getRemindEmail()).isEqualTo(newMail);
		assertThat(user.isVerified()).isTrue();
	}
}
