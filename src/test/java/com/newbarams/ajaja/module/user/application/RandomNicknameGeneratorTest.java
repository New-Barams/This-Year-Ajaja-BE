package com.newbarams.ajaja.module.user.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.RepeatedTest;

class RandomNicknameGeneratorTest {
	@RepeatedTest(10)
	void generate_Success_WithinTwentyLetters() {
		// given
		final int expectedSize = 20;

		// when
		String nickname = RandomNicknameGenerator.generate();

		// then
		assertThat(nickname).hasSizeLessThanOrEqualTo(expectedSize);
	}
}
