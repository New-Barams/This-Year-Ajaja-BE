package me.ajaja.module.plan.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import net.jqwik.api.Arbitraries;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

class RemindInfoTest {
	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.build();

	@Test
	void createRemindInfo_Success() {
		fixtureMonkey.giveMeOne(RemindInfo.class);
	}

	@Test
	@DisplayName("음수인 TotalPeriod로 생성 요청을 하면 예외가 발생한다.")
	void createRemindInfo_Fail_ByTotalPeriod() {
		assertThatThrownBy(() -> fixtureMonkey.giveMeBuilder(RemindInfo.class)
			.set("remindTotalPeriod", Arbitraries.integers().lessOrEqual(-1))
			.sample())
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("음수인 Term으로 생성 요청을 하면 예외가 발생한다.")
	void createRemindInfo_Fail_ByTerm() {
		assertThatThrownBy(() -> fixtureMonkey.giveMeBuilder(RemindInfo.class)
			.set("remindTerm", Arbitraries.integers().lessOrEqual(-1))
			.sample())
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("음수인 Date로 생성 요청을 하면 예외가 발생한다.")
	void createRemindInfo_Fail_ByDate() {
		assertThatThrownBy(() -> fixtureMonkey.giveMeBuilder(RemindInfo.class)
			.set("remindDate", Arbitraries.integers().lessOrEqual(-1))
			.sample())
			.isInstanceOf(IllegalArgumentException.class);
	}
}
