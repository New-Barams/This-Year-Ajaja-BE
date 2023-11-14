package com.newbarams.ajaja.module.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.newbarams.ajaja.common.MonkeySupport;

import jakarta.validation.ConstraintViolationException;

class NicknameTest extends MonkeySupport {
	@Test
	@DisplayName("자동 생성된 닉네임은 20자 이내의 문자를 가지고 있어야 한다.")
	void createWithMonkey_Success_WithLessThanOrEqualTo20letters() {
		// given
		int givenSize = 20;

		// when, then
		assertThatNoException().isThrownBy(() -> {
			Nickname nickname = monkey.giveMeOne(Nickname.class);
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
