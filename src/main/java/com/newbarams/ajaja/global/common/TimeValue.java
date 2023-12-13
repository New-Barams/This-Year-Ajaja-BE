package com.newbarams.ajaja.global.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TimeValue {
	private static final String DEFAULT_TIME_ZONE = "Asia/Seoul";

	private final Instant instant;
	private final ZonedDateTime zonedDateTime;

	public TimeValue(Instant instant) {
		this.instant = instant;
		zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of(DEFAULT_TIME_ZONE));
	}

	public TimeValue() {
		this(Instant.now());
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

	public Date expireIn(long validTime) {
		return new Date(instant.toEpochMilli() + validTime);
	}

	public long getTimeMillis() {
		return instant.toEpochMilli();
	}

	public ZonedDateTime oneMonthLater() {
		return ZonedDateTime.ofInstant(instant.plus(31, ChronoUnit.DAYS), ZoneId.of(DEFAULT_TIME_ZONE));
	}

	public static boolean check(Instant createdAt) {
		return Instant.now().isAfter(createdAt.plus(31, ChronoUnit.DAYS));
	}
}
