package com.newbarams.ajaja.module.plan.domain;

import static com.newbarams.ajaja.module.plan.domain.RemindInfo.RemindTime.*;
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

class RemindInfoTest {
	private FixtureMonkey fixtureMonkey;

	@BeforeEach
	void setUp() {
		fixtureMonkey = FixtureMonkey.builder()
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.plugin(new JakartaValidationPlugin())
			.build();
	}

	@Test
	void createRemindInfo_success() {
		RemindInfo remindInfo = fixtureMonkey.giveMeOne(RemindInfo.class);

		then(remindInfo.getRemindTotalPeriod()).isGreaterThanOrEqualTo(0);
		then(remindInfo.getRemindTerm()).isGreaterThanOrEqualTo(0);
		then(remindInfo.getRemindDate()).isGreaterThanOrEqualTo(0);
		then(remindInfo.getRemindTime()).isIn(MORNING, AFTERNOON, EVENING);
	}

	@Test
	@DisplayName("음수인 TotalPeriod로 생성 요청을 하면 예외가 발생한다.")
	void createRemindInfo_fail_byTotalPeriod() {
		assertThatThrownBy(() -> fixtureMonkey.giveMeBuilder(RemindInfo.class)
			.set("remindTotalPeriod", Arbitraries.integers().lessOrEqual(-1))
			.sample())
			.isInstanceOf(FilterMissException.class);
	}

	@Test
	@DisplayName("음수인 Term으로 생성 요청을 하면 예외가 발생한다.")
	void createRemindInfo_fail_byTerm() {
		assertThatThrownBy(() -> fixtureMonkey.giveMeBuilder(RemindInfo.class)
			.set("remindTerm", Arbitraries.integers().lessOrEqual(-1))
			.sample())
			.isInstanceOf(FilterMissException.class);
	}

	@Test
	@DisplayName("음수인 Date로 생성 요청을 하면 예외가 발생한다.")
	void createRemindInfo_fail_byDate() {
		assertThatThrownBy(() -> fixtureMonkey.giveMeBuilder(RemindInfo.class)
			.set("remindDate", Arbitraries.integers().lessOrEqual(-1))
			.sample())
			.isInstanceOf(FilterMissException.class);
	}
}
