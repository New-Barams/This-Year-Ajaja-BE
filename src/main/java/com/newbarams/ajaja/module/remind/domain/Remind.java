package com.newbarams.ajaja.module.remind.domain;

import java.time.Instant;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Remind extends SelfValidating<Remind> {
	public enum Type {
		PLAN,
		AJAJA
	}

	@NotNull
	private final Long userId;
	@NotNull
	private final Long planId;
	@NotNull
	private final Type type;

	private Info info;
	private Period period;

	public Remind(Long userId, Long planId, Info info, Period period, Type type) {
		this.userId = userId;
		this.planId = planId;
		this.info = info;
		this.period = period;
		this.type = type;
		this.validateSelf();
	}

	public static Remind plan(Long userId, Long planId, Info info, Period period) {
		return new Remind(userId, planId, info, period, Type.PLAN);
	}

	public static Remind ajaja(Long userId, Long planId, Info info, Period period) {
		return new Remind(userId, planId, info, period, Type.AJAJA);
	}

	public boolean isExpired() {
		return this.period.isExpired();
	}

	public Instant getStart() {
		return this.period.getStarts();
	}

	public Instant getEnd() {
		return this.period.getEnds();
	}
}
