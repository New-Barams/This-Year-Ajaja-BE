package com.newbarams.ajaja.module.remind.domain;

import java.time.Instant;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period extends SelfValidating<Period> {
	@Past
	private Instant start;

	@PastOrPresent
	private Instant end;

	public Period(Instant start, Instant end) {
		this.start = start;
		this.end = end;
		this.validateSelf();
	}
}
