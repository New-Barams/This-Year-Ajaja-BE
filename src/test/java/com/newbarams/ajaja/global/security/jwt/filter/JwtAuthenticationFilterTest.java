package com.newbarams.ajaja.global.security.jwt.filter;

import static org.assertj.core.api.Assertions.*;

import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JwtAuthenticationFilterTest {
	static final Pattern GET_ONE_PLAN = Pattern.compile("^/plans/\\d+$");

	@ParameterizedTest
	@ValueSource(strings = {"/plans", "", "/plan", "/", "/plans/1/feedbacks", "/plans/a", "/plans/a1", "/plans/1a"})
	@DisplayName("잘못된 URI가 입력되면 false를 리턴해야 한다.")
	void getPlanFiltering_Fail_WithInvalidUri(String uri) {
		// given

		// when
		boolean shouldBeFalse = GET_ONE_PLAN.matcher(uri).matches();

		// then
		assertThat(shouldBeFalse).isFalse();
	}

	@ParameterizedTest
	@ValueSource(strings = {"/plans/1", "/plans/12", "/plans/123", "/plans/1234", "/plans/12345"})
	@DisplayName("잘못된 URI가 입력되면 false를 리턴해야 한다.")
	void getPlanFiltering_Success_WithInvalidUri(String uri) {
		// given

		// when
		boolean shouldBeTrue = GET_ONE_PLAN.matcher(uri).matches();

		// then
		assertThat(shouldBeTrue).isTrue();
	}
}
