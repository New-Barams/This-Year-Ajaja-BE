package com.newbarams.ajaja.module.feedback.domain;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@Embeddable
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ValidDate {
	private Instant remindDate;

	public ValidDate(Instant remindDate) {
		this.remindDate = remindDate;
	}

	public boolean isExpired() {
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());
		Timestamp deadline = Timestamp.from(this.remindDate.plus(31, ChronoUnit.DAYS));

		return currentDate.before(Timestamp.from(this.remindDate)) || currentDate.after(deadline);
	}
}
