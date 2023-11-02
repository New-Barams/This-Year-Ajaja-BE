package com.newbarams.ajaja.module.user.domain;

import static com.newbarams.ajaja.module.user.domain.User.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintViolationException;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

class UserTest {
	private final FixtureMonkey sut = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.build();

	@Nested
	@DisplayName("이메일 Value 테스트")
	class EmailTest {
		@ParameterizedTest
		@ValueSource(strings = {"gmlwh124@naver.com", "ajaja.net@gamil.com", "123123@kakao.net"})
		@DisplayName("유효한 이메일로 생성하면 예외가 발생하지 않아야 한다.")
		void create_Success_WithValidEmail(String email) {
			// given

			// when, then
			assertThatNoException().isThrownBy(() -> new Email(email));
		}

		@ParameterizedTest
		@ValueSource(strings = {"gmlwh124@", "hejow@kakao", "john@.com", "gmlwh@naver.", "@gmail", "@.", "@gmail.",
			"@.com"})
		@DisplayName("유효하지 않은 이메일로 생성하면 ConstraintViolationException을 던져야 한다.")
		void create_Fail_ByInvalidEmail(String email) {
			// given

			// when, then
			assertThatExceptionOfType(ConstraintViolationException.class)
				.isThrownBy(() -> new Email(email));
		}

		@Test
		@DisplayName("이메일을 검증 완료하면 검증 상태가 true로 변경되어야 한다.")
		void verified_Success_WithChangedResult() {
			// given
			String input = "gmlwh124@naver.com";
			Email email = new Email(input);

			// when
			email.verified();

			// then
			assertThat(email.isVerified()).isTrue();
		}
	}

	@Nested
	@DisplayName("닉네임 Value 테스트")
	class NicknameTest {
		@Test
		@DisplayName("자동 생성된 닉네임은 20자 이내의 문자를 가지고 있어야 한다.")
		void createWithMonkey_Success_WithLessThanOrEqualTo20letters() {
			// given
			int givenSize = 20;

			// when, then
			assertThatNoException().isThrownBy(() -> {
				Nickname nickname = sut.giveMeOne(Nickname.class);
				assertThat(nickname.getNickname()).hasSizeLessThanOrEqualTo(givenSize);
			});
		}

		@Test
		@DisplayName("20자를 초과하면 닉네임으로 생성하면 ConstraintViolationException을 던져야 한다.")
		void create_Fail_ByOver20letters() {
			// given
			String nickname = "imhejowandhejowmoonandjohninenglish";

			// when, then
			assertThatExceptionOfType(ConstraintViolationException.class)
				.isThrownBy(() -> new Nickname(nickname));
		}

		@ParameterizedTest
		@ValueSource(strings = {"hejow", "hejowmoon", "john", "gangster"})
		@DisplayName("20자 이내의 닉네임으로 생성하면 예외가 발생하지 않아야 한다.")
		void create_Success_WithLessThan20letters(String nickname) {
			// given

			// when, then
			assertThatNoException().isThrownBy(() -> new Nickname(nickname));
		}

	}

	@Nested
	@DisplayName("유저 Entity 테스트")
	class UserEntityTest {
		private final Nickname nickname = sut.giveMeOne(Nickname.class);

		@ParameterizedTest
		@ValueSource(strings = {"gmlwh124@naver.com", "ajaja.net@gamil.com", "123123@kakao.net"})
		@DisplayName("유효한 이메일로 유저를 생성하면 예외가 발생하지 않고 기본 상태를 가진다.")
		void create_Success_WithoutExceptionAndNormalState(String input) {
			// given

			// when, then
			assertThatNoException().isThrownBy(() -> {
				User user = new User(nickname, new Email(input));
				assertThat(user.getStatus()).isEqualTo(Status.NORMAL);
			});
		}

		@Test
		@DisplayName("미검증 유저의 이메일을 검증하면 예외가 발생하지 않아야 한다.")
		void verifyEmail_Success_WithoutException() {
			// given
			String input = "gmlwh124@naver.com";
			Email email = new Email(input);

			User user = new User(nickname, email);

			// when, then
			assertThatNoException().isThrownBy(user::verifyEmail);
			assertThat(user.isEmailVerified()).isTrue();
		}

		@Test
		@DisplayName("이미 검증을 실시한 유저는 예외를 던져야 한다.")
		void verifyEmail_Fail_ByAlreadyVerified() {
			// given
			String input = "gmlwh124@naver.com";
			Email email = new Email(input);
			email.verified();

			User user = new User(nickname, email);

			// when, then
			assertThatException().isThrownBy(user::verifyEmail);
		}
	}
}
