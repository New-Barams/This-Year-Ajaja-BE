package com.newbarams.ajaja.module.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

class RandomNicknameGeneratorTest {
	@RepeatedTest(10)
	@DisplayName("무작위로 생성되는 이름은 20자를 넘지 않아야 한다.")
	void generate_Success_WithinTwentyLetters() {
		// given
		final int expectedSize = 20;

		// when
		String nickname = RandomNicknameGenerator.generate();

		// then
		assertThat(nickname).hasSizeLessThanOrEqualTo(expectedSize);
	}
}
