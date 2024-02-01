package me.ajaja.module.user.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintViolationException;
import me.ajaja.common.support.MonkeySupport;

class EmailTest extends MonkeySupport {
	private static final String DEFAULT_EMAIL = "ajaja@me.com";

	@ParameterizedTest
	@ValueSource(strings = {"gmlwh124@naver.com", "ajaja.net@gamil.com", "123123@kakao.net"})
	@DisplayName("유효한 이메일로 생성하면 예외가 발생하지 않아야 한다.")
	void create_Success_WithValidEmail(String email) {
		// given

		// when, then
		assertThatNoException().isThrownBy(() -> Email.init(email));
	}

	@ParameterizedTest
	@MethodSource("badEmails")
	@DisplayName("유효하지 않은 이메일로 생성하면 ConstraintViolationException을 던져야 한다.")
	void create_Fail_ByInvalidEmail(String email) {
		// given

		// when, then
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> Email.init(email));
	}

	@Test
	@DisplayName("같은 이메일로 검증 완료하면 검증 상태만 변경된 새로운 객체를 생성해야 한다.")
	void verified_Success_WithNewInstanceAndOnlyVerifiedChange() {
		// given
		Email email = Email.init(DEFAULT_EMAIL);

		// when
		Email verifiedEmail = email.verified(DEFAULT_EMAIL);

		// then
		assertThat(verifiedEmail.getSignUpEmail()).isEqualTo(email.getSignUpEmail());
		assertThat(verifiedEmail.getRemindEmail()).isEqualTo(email.getRemindEmail());
		assertThat(verifiedEmail.isVerified()).isTrue();
	}

	@Test
	@DisplayName("새로운 리마인드 이메일로 검증을 완료하면 가입 이메일을 제외하고 변경된 새로운 객체를 생성해야 한다.")
	void verified_Success_WithNewInstanceAndOnlyEmailNotChanged() {
		// given
		String old = "gmlwh124@naver.com";
		String young = "hejow124@naver.com";
		Email email = Email.init(old);

		// when
		Email verifiedWithNewRemind = email.verified(young);

		// then
		assertThat(verifiedWithNewRemind).isNotEqualTo(email);
		assertThat(verifiedWithNewRemind.getSignUpEmail()).isEqualTo(email.getSignUpEmail());
		assertThat(verifiedWithNewRemind.getRemindEmail()).isEqualTo(young);
		assertThat(verifiedWithNewRemind.isVerified()).isTrue();
	}

	@ParameterizedTest
	@MethodSource("badEmails")
	@DisplayName("유효하지 않은 이메일로 인증을 완료하려고 하면 예외를 던져야 한다.")
	void verified_Fail_ByBadEmails(String requestEmail) {
		// given
		Email email = Email.init(DEFAULT_EMAIL);

		// when, then
		assertThatThrownBy(() -> email.verified(requestEmail))
			.isInstanceOf(ConstraintViolationException.class);
	}

	@Test
	@DisplayName("같은 리마인드 이메일이지만 미인증 상태라면 검증을 진행할 수 있다.")
	void validateVerifiable_Success_WithSameRemindEmail() {
		// given
		Email email = Email.init(DEFAULT_EMAIL);

		// when, then
		assertThatNoException().isThrownBy(() -> email.validateVerifiable(DEFAULT_EMAIL));
	}

	@Test
	@DisplayName("인증 상태라면 다른 리마인드 이메일일 때 검증을 진행할 수 있다.")
	void validateVerifiable_Success_WithDifferentEmail() {
		// given
		Email email = new Email(DEFAULT_EMAIL, DEFAULT_EMAIL, true);
		String newRemindEmail = "hejow124@naver.com";

		// when, then
		assertThatNoException().isThrownBy(() -> email.validateVerifiable(newRemindEmail));
	}

	@Test
	@DisplayName("인증 완료 상태인데 같은 이메일이라면 예외를 던진다.")
	void validateVerifiable_Fail_ByVerifiedAndSameEmail() {
		// given
		Email email = new Email(DEFAULT_EMAIL, DEFAULT_EMAIL, true);

		// when, then
		assertThatException().isThrownBy(() -> email.validateVerifiable(DEFAULT_EMAIL));
	}

	private static Stream<String> badEmails() {
		return Stream.of(
			"gmlwh124@", "hejow@kakao", "john@.com", "gmlwh@naver.", "@gmail", "@.", "@gmail.", "@.com"
		);
	}
}
