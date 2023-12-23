package com.newbarams.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

class RandomCertificationGeneratorTest {
	@RepeatedTest(10)
	@DisplayName("무작위로 생성되는 인증번호는 6자를 넘어갈 수 없다.")
	void generate_Success_WithinSixLetters() {
		// given
		final int expectedSize = 6;

		// when
		String certification = RandomCertificationGenerator.generate();

		// then
		assertThat(certification).hasSizeLessThanOrEqualTo(expectedSize);
	}
}
