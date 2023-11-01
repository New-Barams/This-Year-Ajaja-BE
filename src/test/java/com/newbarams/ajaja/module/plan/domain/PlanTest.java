package com.newbarams.ajaja.module.plan.domain;

import static org.assertj.core.api.BDDAssertions.*;

import org.junit.jupiter.api.Test;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

class PlanTest {
	@Test
	void createPlan_success() {
		FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.plugin(new JakartaValidationPlugin())
			.build();

		Plan plan = fixtureMonkey.giveMeOne(Plan.class);

		then(plan.getId()).isNotNull();
	}
}
