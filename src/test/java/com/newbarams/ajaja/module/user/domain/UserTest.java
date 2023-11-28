package com.newbarams.ajaja.module.user.domain;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.newbarams.ajaja.common.MonkeySupport;
import com.newbarams.ajaja.global.exception.AjajaException;

class UserTest extends MonkeySupport {
	private final String nickname = "imhejow";

	@ParameterizedTest
	@ValueSource(strings = {"gmlwh124@naver.com", "ajaja.net@gamil.com", "123123@kakao.net"})
	@DisplayName("유효한 이메일로 유저를 생성하면 예외가 발생하지 않는다.")
	void create_Success_WithoutExceptionAndNormalState(String input) {
		// given

		// when, then
		assertThatNoException().isThrownBy(() -> new User(nickname, input, OauthInfo.kakao(1L)));
	}

	@Test
	@DisplayName("같은 리마인드 이메일로 사용자가 이메일 인증을 완료하면 검증 상태만 변경된 새로운 객체를 가져야 한다.")
	void verified_Success_WithSameRemindEmail() {
		// given
		String mail = "gmlwh124@naver.com";
		User user = monkey.giveMeBuilder(User.class)
			.set("email", new Email(mail))
			.sample();

		Email email = user.getEmail();

		// when
		user.verified(mail);

		// then
		assertThat(user.getEmail()).isNotEqualTo(email);
		assertThat(user.getEmail()).usingRecursiveComparison().isNotEqualTo(email);
		assertThat(user.getEmail().isVerified()).isTrue();
		assertThat(user.getEmail().getRemindEmail()).isEqualTo(mail);
	}

	@Test
	@DisplayName("다른 리마인드 이메일로 사용자가 이메일 인증을 완료하면 검증 상태와 리마인드 이메일이 변경된 새로운 객체를 가져야 한다.")
	void verified_Success_WithDifferentRemindEmail() {
		// given
		User user = monkey.giveMeBuilder(User.class)
			.set("email", new Email("gmlwh124@naver.com"))
			.sample();

		Email email = user.getEmail();
		String newMail = "hejow124@naver.com";

		// when
		user.verified(newMail);

		// then
		assertThat(user.getEmail()).isNotEqualTo(email);
		assertThat(user.getEmail()).usingRecursiveComparison().isNotEqualTo(email);
		assertThat(user.getEmail().isVerified()).isTrue();
		assertThat(user.getEmail().getRemindEmail()).isEqualTo(newMail);
	}

	@Test
	void getEmail_Success() {
		// given
		String email = "gmlwh124@naver.com";
		User user = new User(nickname, email, OauthInfo.kakao(1L));

		// when, then
		assertThat(user.defaultEmail()).isEqualTo(email);
		assertThat(user.getRemindEmail()).isEqualTo(email);
	}

	@ParameterizedTest
	@ValueSource(strings = {"kakao", "email", "both"})
	@DisplayName("지원하는 수신 타입이 입력되면 예외 없이 입력된다.")
	void updateReceive_Success_WithSupportType(String type) {
		// given
		String email = "gmlwh124@naver.com";
		User user = new User(nickname, email, OauthInfo.kakao(1L));

		// when, then
		assertThatNoException().isThrownBy(() -> user.updateReceive(type));
	}

	@ParameterizedTest
	@ValueSource(strings = {"gagao", "sms", "line", "none"})
	@DisplayName("지원하지 않는 수신 타입이 입력되면 예외를 던진다.")
	void updateReceive_Fail_ByNotSupportType(String type) {
		// given
		String email = "gmlwh124@naver.com";
		User user = new User(nickname, email, OauthInfo.kakao(1L));

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> user.updateReceive(type))
			.withMessage(NOT_SUPPORT_RECEIVE_TYPE.getMessage());
	}
}
