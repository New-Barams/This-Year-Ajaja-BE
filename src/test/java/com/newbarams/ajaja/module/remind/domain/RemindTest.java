package com.newbarams.ajaja.module.remind.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

class RemindTest {
	private final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.build();

	@Nested
	@DisplayName("리마인드 받을 메세지를 테스트한다.")
	class ContentTest {

		@Test
		void setContent_Success_WithNoException() {
			// given
			String content = "title";

			// when,then
			assertThatNoException()
				.isThrownBy(() -> fixtureMonkey.giveMeBuilder(Info.class).set("content", content).sample());
		}

		@Test
		@DisplayName("빈 메세지를 설정할 경우 예외를 던진다.")
		void setEmptyContent_Fail_ValidationException() {
			// given
			String emptyString = " ";

			// when,then
			assertThatException()
				.isThrownBy(() -> fixtureMonkey.giveMeBuilder(Info.class).set("content", emptyString).sample());
		}
	}

	@Nested
	@DisplayName("리마인드 시작일과 종료일을 테스트한다.")
	class PeriodTest {
		Instant futureTime = Year.of(2050)
			.atMonth(6)
			.atDay(18)
			.atTime(20, 30)
			.atZone(ZoneId.of("Asia/Seoul"))
			.toInstant();

		Instant pastTime = Year.of(2000)
			.atMonth(6)
			.atDay(18)
			.atTime(20, 30)
			.atZone(ZoneId.of("Asia/Seoul"))
			.toInstant();

		@Test
		void setPeriod_Success_WithNoException() {
			assertThatNoException().isThrownBy(
				() -> fixtureMonkey.giveMeBuilder(Period.class).set("start", pastTime).set("end", pastTime).sample());
		}

		@Test
		@DisplayName("시작 날짜를 미래로 설정할 경우 예외를 던진다.")
		void setStartPeriod_Fail_ByValidationException() {
			assertThatException()
				.isThrownBy(() -> fixtureMonkey.giveMeBuilder(Period.class).set("start", futureTime).sample());
		}

		@Test
		@DisplayName("과거 날짜를 미래로 설정할 경우 예외를 던진다.")
		void setEndPeriod_Fail_ByValidationException() {
			assertThatException()
				.isThrownBy(() -> fixtureMonkey.giveMeBuilder(Period.class).set("end", futureTime).sample());
		}
	}

	@Nested
	@DisplayName("종류에 따른 리마인드를 생성한다.")
	class MakeRemindTest {

		@Test
		void makePlanRemind() {
			// given
			Info info = fixtureMonkey.giveMeOne(Info.class);
			Period period = fixtureMonkey.giveMeOne(Period.class);

			// when,then
			assertThatNoException().isThrownBy(
				() -> Remind.plan(1L, 1L, info, period));

		}

		@Test
		void makeAjajaRemind() {
			// given
			Info info = fixtureMonkey.giveMeOne(Info.class);
			Period period = fixtureMonkey.giveMeOne(Period.class);

			// when,then
			assertThatNoException().isThrownBy(
				() -> Remind.ajaja(1L, 1L, info, period));
		}
	}
}
