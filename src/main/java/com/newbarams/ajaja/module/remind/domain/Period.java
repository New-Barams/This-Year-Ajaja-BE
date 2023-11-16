package com.newbarams.ajaja.module.remind.domain;

import java.time.Instant;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period extends SelfValidating<Period> {
	private Instant start;
	private Instant end;

	public Period(Instant start, Instant end) {
		this.start = start;
		this.end = end;
		this.validateSelf();
	}
}
