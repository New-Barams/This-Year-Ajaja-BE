package me.ajaja.global.common;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BaseTimeTest {
	private static final long ONE_DAY = 60 * 60 * 24 * 1000L;
	private static final long ONE_SECOND = 60 * 1000L;

	@ParameterizedTest
	@ValueSource(longs = {
		ONE_DAY,
		ONE_DAY * 2,
		ONE_DAY * 3,
	})
	@DisplayName("지금으로부터 3일 이내라면 true를 리턴해야 한다.")
	void isWithinThreeDays_Success(long days) {
		// given
		Date threeDaysBefore = new Date(new Date().getTime() - days);
		Date threeDaysAfter = new Date(new Date().getTime() + days);

		// when
		boolean withinThreeDaysBefore = BaseTime.now().isWithin3Days(threeDaysBefore);
		boolean withinThreeDaysAfter = BaseTime.now().isWithin3Days(threeDaysAfter);

		// then
		assertThat(withinThreeDaysBefore).isTrue();
		assertThat(withinThreeDaysAfter).isTrue();
	}

	@ParameterizedTest
	@ValueSource(longs = {
		ONE_DAY * 4 + ONE_SECOND, // to avoid equal case
		ONE_DAY * 5,
		ONE_DAY * 6,
		ONE_DAY * 7,
	})
	@DisplayName("지금으로부터 3일 이후라면 false를 리턴해야 한다.")
	void isWithinThreeDays_Fail(long days) {
		// given
		Date fourDaysBefore = new Date(new Date().getTime() - days);
		Date fourDaysAfter = new Date(new Date().getTime() + days);

		// when
		boolean withinThreeDaysBefore = BaseTime.now().isWithin3Days(fourDaysBefore);
		boolean withinThreeDaysAfter = BaseTime.now().isWithin3Days(fourDaysAfter);

		// then
		assertThat(withinThreeDaysBefore).isFalse();
		assertThat(withinThreeDaysAfter).isFalse();
	}
}
