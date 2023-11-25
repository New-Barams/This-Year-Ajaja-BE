package com.newbarams.ajaja.global.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeValue {
	Instant instant = Instant.now();
	ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

	public int getMonth() {
		return zonedDateTime.getMonthValue();
	}

	public int getDate() {
		return zonedDateTime.getDayOfMonth();
	}

	public int getMonthFrom(Instant instant) {
		return instant.atZone(ZoneId.systemDefault()).getMonthValue();
	}

	public int getDateFrom(Instant instant) {
		return instant.atZone(ZoneId.systemDefault()).getDayOfMonth();
	}
}
