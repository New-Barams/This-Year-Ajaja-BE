package me.ajaja.module.remind.domain;

import static me.ajaja.module.remind.domain.Remind.Type.*;

import java.util.Objects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import me.ajaja.global.common.BaseTime;
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

	public static Remind plan(Long userId, String sendType, Long planId, String message, BaseTime time) {
		return of(userId, sendType, planId, message, PLAN, time);
	}

	public static Remind ajaja(Long userId, String sendType, Long planId, String message, BaseTime time) {
		return of(userId, sendType, planId, message, AJAJA, time);
	}

	private static Remind of(Long userId, String sendType, Long planId, String message, Type type, BaseTime time) {
		Receiver receiver = Receiver.init(userId, sendType);
		Target target = Target.init(planId);
		return new Remind(receiver, target, message, type, time.getMonth(), time.getDate());
	}

	public Remind asPlan(String sendType, BaseTime when) {
		return Remind.plan(getUserId(), sendType, getPlanId(), this.message, when);
	}

	public boolean isValidNumber() {
		return !Objects.equals(this.getPhoneNumber(), "01000000000");
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
