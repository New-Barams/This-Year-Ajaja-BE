package com.newbarams.ajaja.module.feedback.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

class FeedbackTest {
	private FixtureMonkey fixtureMonkey;

	@BeforeEach
	void setUp() {
		fixtureMonkey = FixtureMonkey.builder()
			.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
			.plugin(new JakartaValidationPlugin())
			.build();
	}

	@Test
	@DisplayName("유저id 값이 null일 경우 예외를 던진다.")
	void userIdTest_Fail_ByValidationException() {
		assertThatException()
			.isThrownBy(() -> fixtureMonkey.giveMeBuilder(Feedback.class).set("userId", null).sample());
	}

	@Test
	@DisplayName("계획id 값이 null일 경우 예외를 던진다.")
	void planIdTest_Fail_ByValidationException() {
		assertThatException()
			.isThrownBy(() -> fixtureMonkey.giveMeBuilder(Feedback.class).set("planId", null).sample());
	}
}
