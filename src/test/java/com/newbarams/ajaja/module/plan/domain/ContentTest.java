package com.newbarams.ajaja.module.plan.domain;

import static org.assertj.core.api.BDDAssertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.jqwik.api.Arbitraries;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.exception.FilterMissException;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

class ContentTest {
	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.build();

	@Test
	void createContent_Success() {
		fixtureMonkey.giveMeOne(Content.class);
	}

	@Test
	@DisplayName("title의 글자수가 초과되면 예외가 발생한다.")
	void createContent_Fail_ByTitle() {
		assertThatThrownBy(() -> fixtureMonkey.giveMeBuilder(Content.class)
			.set("title", Arbitraries.strings().ofMinLength(51))
			.sample())
			.isInstanceOf(FilterMissException.class);
	}

	@Test
	@DisplayName("description의 글자수가 초과되면 예외가 발생한다.")
	void createContent_Fail_ByDescription() {
		assertThatThrownBy(() -> fixtureMonkey.giveMeBuilder(Content.class)
			.set("description", Arbitraries.strings().ofMinLength(301))
			.sample())
			.isInstanceOf(FilterMissException.class);
	}
}
