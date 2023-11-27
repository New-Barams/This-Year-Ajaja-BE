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
	private Instant starts;
	private Instant ends;

	public Period(Instant starts, Instant ends) {
		this.starts = starts;
		this.ends = ends;
		this.validateSelf();
	}

	public boolean isExpired() {
		return Instant.now().isAfter(this.ends);
	}
}
