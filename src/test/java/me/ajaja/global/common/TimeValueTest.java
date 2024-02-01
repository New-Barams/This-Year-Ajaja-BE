package me.ajaja.global.common;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class TimeValueTest {
	@Test
	void isWithinThreeDays_Success() {
		// given
		Date threeDaysBefore = new Date(new Date().getTime() - 60 * 60 * 24 * 3 * 1000L);

		// when
		boolean withinThreeDays = TimeValue.now().isWithinThreeDays(threeDaysBefore);

		// then
		assertThat(withinThreeDays).isTrue();
	}

	@Test
	void isWithinThreeDays_Fail() {
		// given
		Date fourDaysBefore = new Date(new Date().getTime() - 60 * 60 * 24 * 4 * 1000L);

		// when
		boolean withinThreeDays = TimeValue.now().isWithinThreeDays(fourDaysBefore);

		// then
		assertThat(withinThreeDays).isFalse();
	}
}
