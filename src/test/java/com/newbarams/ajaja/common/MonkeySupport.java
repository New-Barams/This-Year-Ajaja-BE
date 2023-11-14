package com.newbarams.ajaja.common;

import java.util.List;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

/**
 * Supporting Monkey Which is based on ConstructorProperties and NEVER return null.
 */
public abstract class MonkeySupport {
	protected final FixtureMonkey monkey = FixtureMonkey.builder()
		.objectIntrospector(new FailoverIntrospector(
			List.of(
				FieldReflectionArbitraryIntrospector.INSTANCE,
				ConstructorPropertiesArbitraryIntrospector.INSTANCE
			)
		))
		.plugin(new JakartaValidationPlugin())
		.defaultNotNull(true)
		.build();
}
