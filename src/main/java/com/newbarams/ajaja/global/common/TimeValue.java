package com.newbarams.ajaja.global.common;

import java.time.Duration;
import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import lombok.Getter;

@Getter
public class TimeValue {
	private static final String DEFAULT_TIME_ZONE = "Asia/Seoul";
	private static final int THREE_DAYS = 3;
	private static final int LAST_MONTH = 12;

	private final Instant instant;
	private final ZonedDateTime zonedDateTime;

	public TimeValue(Instant instant) {
		this.instant = instant;
		zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of(DEFAULT_TIME_ZONE));
	}

	public static TimeValue now() {
		return new TimeValue(Instant.now());
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

	public Date expireIn(long validTime) {
		return new Date(instant.toEpochMilli() + validTime);
	}

	public long getTimeMillis() {
		return instant.toEpochMilli();
	}

	public ZonedDateTime oneMonthLater() {
		return zonedDateTime.getMonthValue() == LAST_MONTH
			? parseDateTime(12, 31, 23, 59) : zonedDateTime.plusMonths(1);
	}

	public boolean isWithinThreeDays(Date expireIn) {
		Duration between = Duration.between(instant, expireIn.toInstant());
		return Math.abs(between.toDays()) <= THREE_DAYS;
	}

	public boolean isAfter(TimeValue time) {
		return zonedDateTime.isAfter(time.zonedDateTime);
	}

	public boolean isBetween(TimeValue time) {
		return this.zonedDateTime.isAfter(time.zonedDateTime)
			&& this.zonedDateTime.isBefore(time.oneMonthLater());
	}

	private ZonedDateTime parseDateTime(int month, int date, int hour, int minute) {
		return Year.of(zonedDateTime.getYear())
			.atMonth(month)
			.atDay(date)
			.atTime(hour, minute)
			.atZone(ZoneId.of(DEFAULT_TIME_ZONE));
	}

	public boolean isExpired() {
		return zonedDateTime.isAfter(zonedDateTime.plusMonths(1));
	}

	public static TimeValue parse(int year, int month, int date, int hour) {
		Instant instant = Year.of(year)
			.atMonth(month)
			.atDay(date)
			.atTime(hour, 0)
			.atZone(ZoneId.of(DEFAULT_TIME_ZONE))
			.toInstant();

		return new TimeValue(instant);
	}
}
