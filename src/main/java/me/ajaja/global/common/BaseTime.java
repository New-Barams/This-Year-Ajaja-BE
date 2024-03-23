package me.ajaja.global.common;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import lombok.Getter;

@Getter
public final class BaseTime {
	private static final ZoneId DEFAULT_TIME_ZONE = ZoneId.of("Asia/Seoul");

	private static final int THREE_DAYS = 3;
	private static final int DECEMBER = 12;

	private final Instant instant;
	private final ZonedDateTime zonedDateTime;

	public BaseTime(Instant instant) {
		this.instant = instant;
		zonedDateTime = ZonedDateTime.ofInstant(instant, DEFAULT_TIME_ZONE);
	}

	public static BaseTime now() {
		return new BaseTime(Instant.now());
	}

	public static BaseTime parse(int year, int month, int date, int hour) {
		ZonedDateTime dateTime = ZonedDateTime.of(parseDateTime(year, month, date, hour, 0), DEFAULT_TIME_ZONE);
		return new BaseTime(dateTime.toInstant());
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

	public long getTimeMillis() {
		return instant.toEpochMilli();
	}

	public Date expireIn(long validTime) {
		return new Date(instant.toEpochMilli() + validTime);
	}

	public ZonedDateTime oneMonthLater() {
		return getMonth() == DECEMBER ? lastDayOfYear() : zonedDateTime.plusMonths(1);
	}

	private ZonedDateTime lastDayOfYear() {
		LocalDateTime dateTime = parseDateTime(getYear(), 12, 31, 23, 59);
		return ZonedDateTime.of(dateTime, DEFAULT_TIME_ZONE);
	}

	public boolean isWithin3Days(Date expireIn) {
		Duration between = Duration.between(instant, expireIn.toInstant());
		return Math.abs(between.toDays()) <= THREE_DAYS;
	}

	public boolean isAfter(BaseTime time) {
		return zonedDateTime.isAfter(time.zonedDateTime);
	}

	public boolean isWithinAMonth(BaseTime time) {
		return zonedDateTime.isBefore(time.oneMonthLater()) && zonedDateTime.isAfter(time.zonedDateTime);
	}

	public long betweenDays(BaseTime compare) {
		return Duration.between(this.instant, compare.instant).toDays();
	}

	private static LocalDateTime parseDateTime(int year, int month, int date, int hour, int minute) {
		return LocalDateTime.of(year, month, date, hour, minute);
	}
}
