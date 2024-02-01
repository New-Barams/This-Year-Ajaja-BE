package me.ajaja.module.remind.domain;

import java.beans.ConstructorProperties;
import java.time.Instant;

import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Period extends SelfValidating<Period> {
	private final Instant starts;
	private final Instant ends;

	@ConstructorProperties({"starts", "ends"})
	public Period(Instant starts, Instant ends) {
		this.starts = starts;
		this.ends = ends;
		this.validateSelf();
	}

	public boolean isExpired() {
		return Instant.now().isAfter(this.ends);
	}
}
