package me.ajaja.module.remind.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Remind extends SelfValidating<Remind> {
	public enum Type {
		PLAN,
		AJAJA
	}

	private final Receiver receiver;
	private final Target target;
	private final Type type;

	@NotBlank
	@Size(max = 255)
	private final String message;
	private final RemindDate remindDate;

	public Remind(Receiver receiver, Target target, String message, Type type, int remindMonth, int remindDay) {
		this.receiver = receiver;
		this.target = target;
		this.message = message;
		this.type = type;
		this.remindDate = new RemindDate(remindMonth, remindDay);
		this.validateSelf();
	}

	public static Remind plan(Long userId, String remindType, Long planId, String message, int remindMonth,
		int remindDate) {
		return new Remind(
			new Receiver(userId, remindType, null, null),
			new Target(planId, null), message, Type.PLAN, remindMonth, remindDate);
	}

	public static Remind ajaja(Long userId, String remindType, Long planId, String message, int remindMonth,
		int remindDate) {
		return new Remind(
			new Receiver(userId, remindType, null, null),
			new Target(planId, null), message, Type.AJAJA, remindMonth, remindDate);
	}

	public Long getUserId() {
		return this.receiver.getId();
	}

	public String getRemindType() {
		return this.receiver.getType().name();
	}

	public String getEmail() {
		return this.receiver.getEmail();
	}

	public String getPhoneNumber() {
		return this.receiver.getPhoneNumber();
	}

	public String getTitle() {
		return this.target.getTitle();
	}

	public Long getPlanId() {
		return this.target.getId();
	}
}
