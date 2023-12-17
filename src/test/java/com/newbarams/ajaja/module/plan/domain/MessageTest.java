package com.newbarams.ajaja.module.plan.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.jqwik.api.Arbitraries;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

class MessageTest {
	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.build();

	@Test
	void createMessage_Success() {
		assertThatNoException()
			.isThrownBy(() -> fixtureMonkey.giveMeOne(Message.class));
	}

	@Test
	@DisplayName("content의 글자수가 초과되면 예외가 발생한다.")
	void createMessage_Fail_ByContent() {
		assertThatThrownBy(() -> fixtureMonkey.giveMeBuilder(Message.class)
			.set("content", Arbitraries.strings().ofMinLength(256))
			.sample())
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("비어있는 content로 생성 요청을 하면 예외가 발생한다.")
	void createMessage_Fail_ByEmptyContent() {
		assertThatThrownBy(() -> fixtureMonkey.giveMeBuilder(Message.class)
			.set("content", " ")
			.sample())
			.isInstanceOf(IllegalArgumentException.class);
	}
}
