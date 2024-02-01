package me.ajaja.common.support;

import java.util.List;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FailoverIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

/**
 * Supports Monkey Which is based on ConstructorProperties and Reflection <br>
 * NEVER return null.
 * @author hejow
 */
public abstract class MonkeySupport {
	protected final FixtureMonkey sut = FixtureMonkey.builder()
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
