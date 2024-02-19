package me.ajaja.module.remind.domain;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import me.ajaja.common.support.MonkeySupport;

class RemindTest extends MonkeySupport {

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

		@Test
		void makePlanRemind() {
			// when,then
			assertThatNoException().isThrownBy(
				() -> Remind.plan(1L, "KAKAO", 1L, "화이팅", 3, 15));

		}

		@Test
		void makeAjajaRemind() {
			// when,then
			assertThatNoException().isThrownBy(
				() -> Remind.ajaja(1L, "KAKAO", 1L, "화이팅", 3, 15));
		}
	}
}
