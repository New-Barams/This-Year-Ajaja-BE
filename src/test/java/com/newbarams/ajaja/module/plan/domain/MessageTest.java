package com.newbarams.ajaja.module.plan.domain;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.jqwik.api.Arbitraries;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.exception.FilterMissException;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

class MessageTest {
	private FixtureMonkey fixtureMonkey;

	@BeforeEach
	void setUp() {
		fixtureMonkey = FixtureMonkey.builder()
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.plugin(new JakartaValidationPlugin())
			.build();
	}

	@Test
	void createMessage_success() {
		Message message = fixtureMonkey.giveMeOne(Message.class);

		then(message.getContent().length()).isLessThanOrEqualTo(255);
		then(message.getIndex()).isGreaterThanOrEqualTo(0);
	}

	@Test
	@DisplayName("content의 글자수가 초과되면 예외가 발생한다.")
	void createMessage_fail_byContent() {
		assertThatThrownBy(() -> fixtureMonkey.giveMeBuilder(Message.class)
			.set("content", Arbitraries.strings().ofMinLength(256))
			.sample())
			.isInstanceOf(FilterMissException.class);
	}

	@Test
	@DisplayName("0보다 작은 index로 생성 요청을 하면 예외가 발생한다.")
	void createMessage_fail_byIndex() {
		assertThatThrownBy(() -> fixtureMonkey.giveMeBuilder(Message.class)
			.set("index", Arbitraries.integers().lessOrEqual(-1))
			.sample())
			.isInstanceOf(FilterMissException.class);
	}
}
