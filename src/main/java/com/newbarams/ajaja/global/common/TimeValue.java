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

	private final Instant instant;
	private final ZonedDateTime zonedDateTime;

	public TimeValue(Instant instant) {
		this.instant = instant;
		zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of(DEFAULT_TIME_ZONE));
	}

	public TimeValue() {
		this(Instant.now());
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
		return zonedDateTime.plusMonths(1);
	}

	public boolean isWithinThreeDays(Date expireIn) {
		Duration between = Duration.between(instant, expireIn.toInstant());
		return Math.abs(between.toDays()) <= THREE_DAYS;
	}

	public boolean isAfter(TimeValue time) {
		return zonedDateTime.isAfter(time.zonedDateTime);
	}

	public boolean isBetween(int month, int date, int time) {
		ZonedDateTime dateTime = parseDateTime(month, date, time);
		return this.zonedDateTime.isAfter(dateTime) && this.zonedDateTime.isBefore(dateTime.plusMonths(1));
	}

	public ZonedDateTime parseDateTime(int month, int date, int time) {
		return Year.of(zonedDateTime.getYear())
			.atMonth(month)
			.atDay(date)
			.atTime(time, 0)
			.atZone(ZoneId.of("Asia/Seoul"));
	}

	public boolean isExpired() {
		return zonedDateTime.isAfter(zonedDateTime.plusMonths(1));
	}

	public static Instant parseInstant(int year, int month, int date, int time) {
		return Instant.parse(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", date) + "T"
			+ String.format("%02d", time) + ":00:00Z").atZone(ZoneId.of("Asia/Seoul")).toInstant();
	}
}
