package com.newbarams.ajaja.common;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

/**
 * Supporting Mock test with Fixture-Monkey.
 * Monkey is based on ConstructorProperties and NEVER return null.
 */
@ExtendWith(MockitoExtension.class)
public abstract class MockTestSupport {

	protected final FixtureMonkey monkey = FixtureMonkey.builder()
		.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.defaultNotNull(true)
		.build();
}
