package com.newbarams.ajaja.module.remind.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class Remind extends SelfValidating<Remind> {
	public enum Type {
		PLAN,
		AJAJA
	}

	private UserInfo userInfo;
	private PlanInfo planInfo;
	private final Type type;

	@NotBlank
	@Size(max = 255)
	private final String message;
	private final RemindDate remindDate;

	public Remind(UserInfo userInfo, PlanInfo planInfo, String message, Type type, int remindMonth, int remindDay) {
		this.userInfo = userInfo;
		this.planInfo = planInfo;
		this.message = message;
		this.type = type;
		this.remindDate = new RemindDate(remindMonth, remindDay);
		this.validateSelf();
	}

	public static Remind plan(Long userId, Long planId, String message, int remindMonth, int remindDate) {
		return new Remind(new UserInfo(userId, null), new PlanInfo(planId, null), message, Type.PLAN, remindMonth,
			remindDate);
	}

	public static Remind ajaja(Long userId, Long planId, String message, int remindMonth, int remindDate) {
		return new Remind(new UserInfo(userId, null), new PlanInfo(planId, null), message, Type.AJAJA, remindMonth,
			remindDate);
	}

	public Long getUserId() {
		return this.userInfo.getId();
	}

	public String getEmail() {
		return this.userInfo.getEmail();
	}

	public String getTitle() {
		return this.planInfo.getTitle();
	}

	public Long getPlanId() {
		return this.planInfo.getId();
	}
}
