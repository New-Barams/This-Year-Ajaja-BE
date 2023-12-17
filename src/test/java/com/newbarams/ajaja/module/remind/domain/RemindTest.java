package com.newbarams.ajaja.module.remind.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import net.jqwik.api.Arbitraries;

import com.newbarams.ajaja.common.support.MonkeySupport;

class RemindTest extends MonkeySupport {
	@Nested
	@DisplayName("리마인드 받을 메세지를 테스트한다.")
	class ContentTest {
		@Test
		@DisplayName("255자가 넘는 메세지를 설정할 경우 예외를 던진다.")
		void setContentTest_Fail_ByValidationException() {
			// given
			int lengthOverMax = 256;

			// when,then
			Assertions.assertThatException().isThrownBy(
				() -> sut.giveMeBuilder(Info.class)
					.set("content", Arbitraries.strings().ofMinLength(lengthOverMax))
					.sample());
		}

		@Test
		void setContent_Success_WithNoException() {
			// given
			String content = "title";

			// when,then
			assertThatNoException()
				.isThrownBy(() -> sut.giveMeBuilder(Info.class).set("content", content).sample());
		}

		@Test
		@DisplayName("빈 메세지를 설정할 경우 예외를 던진다.")
		void setEmptyContent_Fail_ValidationException() {
			// given
			String emptyString = " ";

			// when,then
			assertThatException()
				.isThrownBy(() -> sut.giveMeBuilder(Info.class).set("content", emptyString).sample());
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
				() -> sut.giveMeBuilder(Period.class).set("start", pastTime).set("end", pastTime).sample());
		}
	}

	@Nested
	@DisplayName("종류에 따른 리마인드를 생성한다.")
	class MakeRemindTest {
		private Info info = sut.giveMeOne(Info.class);

		@Test
		void makePlanRemind() {
			// when,then
			assertThatNoException().isThrownBy(
				() -> Remind.plan(1L, 1L, info, 3, 15));

		}

		@Test
		void makeAjajaRemind() {
			// when,then
			assertThatNoException().isThrownBy(
				() -> Remind.ajaja(1L, 1L, info, 3, 15));
		}
	}
}
