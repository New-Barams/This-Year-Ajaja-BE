package com.newbarams.ajaja.module.plan.domain;

import static com.newbarams.ajaja.module.plan.exception.ErrorMessage.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

class PlanTest {
	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.build();

	@Test
	void createPlan_Success() {
		fixtureMonkey.giveMeOne(Plan.class);
	}

	@Test
	@DisplayName("삭제가능한 기간일 경우 작성한 계획을 삭제할 수 있다.")
	void deletePlan_Success() {
		PlanStatus planStatus = new PlanStatus(true);

		Plan plan = fixtureMonkey.giveMeBuilder(Plan.class)
			.set("status", planStatus)
			.sample();

		plan.delete("Thu JAN 09 2023");

		assertThat(plan.getStatus().isDeleted()).isEqualTo(true);
	}

	@Test
	@DisplayName("삭제가능한 기간이 아닌 경우 계획을 삭제할 수 없다.")
	void deletePlan_Fail_By_Date() {
		PlanStatus planStatus = new PlanStatus(true);

		Plan plan = fixtureMonkey.giveMeBuilder(Plan.class)
			.set("status", planStatus)
			.sample();

		assertThatThrownBy(() -> plan.delete("Thu NOV 09 2023"))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage(INVALID_UPDATABLE_DATE.getMessage());
	}
}
