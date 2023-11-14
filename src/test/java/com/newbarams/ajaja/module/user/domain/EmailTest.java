package com.newbarams.ajaja.module.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintViolationException;

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
	@MethodSource("badEmails")
	@DisplayName("유효하지 않은 이메일로 생성하면 ConstraintViolationException을 던져야 한다.")
	void create_Fail_ByInvalidEmail(String email) {
		// given

		// when, then
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> new Email(email));
	}

	@Test
	@DisplayName("리마인드용 이메일을 변경하면 리마인드 필드만 변경된 새로운 객체를 생성해야 한다.")
	void newRemind_Success_WithNewInstanceAndOnlyRemindEmailChange() {
		// given
		String old = "gmlwh124@naver.com";
		String young = "hejow124@naver.com";
		Email oldEmail = new Email(old);

		// when
		Email youngEmail = oldEmail.newRemind(young);

		// then
		assertThat(youngEmail.getEmail()).isEqualTo(oldEmail.getEmail());
		assertThat(youngEmail.getRemindEmail()).isEqualTo(young);
		assertThat(youngEmail.isVerified()).isEqualTo(oldEmail.isVerified());
	}

	@ParameterizedTest
	@MethodSource("badEmails")
	@DisplayName("변경될 리마인드 이메일이 유효하지 않으면 예외를 던져야 한다.")
	void newRemind_Fail_ByBadEmails(String badEmail) {
		// given
		String input = "gmlwh124@naver.com";
		Email email = new Email(input);

		// when, then
		assertThatThrownBy(() -> email.newRemind(badEmail))
			.isInstanceOf(ConstraintViolationException.class);
	}

	@Test
	@DisplayName("이메일을 검증 완료하면 검증 상태만 변경된 새로운 객체를 생성해야 한다.")
	void verified_Success_WithNewInstanceAndOnlyVerifiedChange() {
		// given
		String input = "gmlwh124@naver.com";
		Email email = new Email(input);

		// when
		Email verifiedEmail = email.verified();

		// then
		assertThat(verifiedEmail.getEmail()).isEqualTo(email.getEmail());
		assertThat(verifiedEmail.getRemindEmail()).isEqualTo(email.getRemindEmail());
		assertThat(verifiedEmail.isVerified()).isTrue();
	}

	private static Stream<String> badEmails() {
		return Stream.of("gmlwh124@", "hejow@kakao", "john@.com", "gmlwh@naver.", "@gmail", "@.", "@gmail.",
			"@.com");
	}
}
