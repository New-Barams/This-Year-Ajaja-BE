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

	private final Info info;

	private final Instant remindDate;

	public Remind(Long userId, Long planId, Info info, Type type, Instant remindDate) {
		this.userId = userId;
		this.planId = planId;
		this.info = info;
		this.type = type;
		this.remindDate = remindDate;
		this.validateSelf();
	}

	public static Remind plan(Long userId, Long planId, Info info) {
		return new Remind(userId, planId, info, Type.PLAN, null);
	}

	public static Remind ajaja(Long userId, Long planId, Info info) {
		return new Remind(userId, planId, info, Type.AJAJA, null);
	}

	public String getContent() {
		return info.getContent();
	}
}
