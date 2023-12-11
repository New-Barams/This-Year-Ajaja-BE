package com.newbarams.ajaja.global.common;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class TimeValue {
	private final Instant instant;
	private final ZonedDateTime zonedDateTime;

	public TimeValue() {
		this.instant = Instant.now();
		zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
	}

	public TimeValue(Instant instant) {
		this.instant = instant;
		zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
	}

	public Instant now() {
		return instant;
	}

	public int getYear() {
		return zonedDateTime.getYear();
	}

	public int getMonth() {
		return zonedDateTime.getMonthValue();
	}

	public int getDate() {
		return zonedDateTime.getDayOfMonth();
	}

	public ZonedDateTime oneMonthLater() {
		return ZonedDateTime.ofInstant(instant.plus(31, ChronoUnit.DAYS), ZoneId.of("Asia/Seoul"));
	}

	public Timestamp toTimeStamp() {
		return Timestamp.valueOf(instant.toString());
	}

	public LocalDateTime toLocalDateTime() {
		return zonedDateTime.toLocalDateTime();
	}

	public Instant parseInstant(int remindMonth, int remindDate, int remindTime) {
		return Instant.parse(
			getYear() + "-" + String.format("%02d", remindMonth) + "-" + String.format("%02d", remindDate) + "T"
				+ String.format("%02d", remindTime) + ":00:00Z");
	}

	public static boolean check(Instant createdAt) {
		return Instant.now().isAfter(createdAt.plus(31, ChronoUnit.DAYS));
	}
}
