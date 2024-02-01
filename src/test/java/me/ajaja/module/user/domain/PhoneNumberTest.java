package me.ajaja.module.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.validation.ConstraintViolationException;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.global.exception.ErrorCode;

class PhoneNumberTest {

	@Test
	@DisplayName("국제 번호로 초기화하면 국내 휴대폰 번호 형식으로 변경되어야 한다.")
	void init_Success_ToMobileFormat() {
		// given
		String contact = "+82 1012345678";

		// when, then
		assertThatNoException().isThrownBy(() -> PhoneNumber.init(contact));
	}

	@ParameterizedTest
	@ValueSource(strings = {"01012345678", "0101234567"})
	@DisplayName("전화번호로 10~11자리가 입력되면 예외를 던지지 않아야 한다.")
	void create_Success_WithoutException(String number) {
		// given

		// when, then
		assertThatNoException().isThrownBy(() -> new PhoneNumber(number));
	}

	@ParameterizedTest
	@NullAndEmptySource
	@DisplayName("전화번호로 빈 값이나 null이 들어오면 예외를 던져야 한다.")
	void create_Fail_ByEmptyAndNull(String number) {
		// given

		// when, then
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> new PhoneNumber(number));
	}

	@ParameterizedTest
	@ValueSource(strings = {"010123456789", "010123456"})
	@DisplayName("전화번호로 10자리 미만 혹은 11자리 초과하면 예외를 던져야 한다.")
	void create_Fail_ByOutRangedInput(String number) {
		// given

		// when, then
		assertThatExceptionOfType(ConstraintViolationException.class)
			.isThrownBy(() -> new PhoneNumber(number));
	}

	@ParameterizedTest
	@ValueSource(strings = {"010a2345678", "0c01234567", "0101-345678", "0101ㄱ345678"})
	@DisplayName("전화번호로 숫자 외에 다른 값을 입력하면 예외를 던져야 한다.")
	void create_Fail_ByInvalidTypeInput(String number) {
		// given

		// when, then
		assertThatExceptionOfType(AjajaException.class)
			.isThrownBy(() -> new PhoneNumber(number))
			.withMessage(ErrorCode.NON_NUMERIC_INPUT.getMessage());
	}
}
