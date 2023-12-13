package com.newbarams.ajaja.module.remind.domain;

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

	private final int remindMonth;
	private final int remindDate;

	public Remind(Long userId, Long planId, Info info, Type type, int remindMonth, int remindDate) {
		this.userId = userId;
		this.planId = planId;
		this.info = info;
		this.type = type;
		this.remindMonth = remindMonth;
		this.remindDate = remindDate;
		this.validateSelf();
	}

	public static Remind plan(Long userId, Long planId, Info info, int remindMonth, int remindDate) {
		return new Remind(userId, planId, info, Type.PLAN, remindMonth, remindDate);
	}

	public static Remind ajaja(Long userId, Long planId, Info info, int remindMonth, int remindDate) {
		return new Remind(userId, planId, info, Type.AJAJA, remindMonth, remindDate);
	}

	public String getContent() {
		return info.getContent();
	}
}
