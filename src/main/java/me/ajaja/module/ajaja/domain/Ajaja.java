package me.ajaja.module.ajaja.domain;

import java.util.Objects;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Ajaja {
	private static final Long DEFAULT_TARGET_ID = -1L;
	private static final Long DEFAULT_USER_ID = -1L;

	public enum Type {
		PLAN,
		FOOTPRINT, // 회고
		DEFAULT
	}

	private Long id;
	private Target target;
	private Receiver receiver;
	private boolean canceled;
	private Type type;
	private Long count;

	private Ajaja(String title, Long targetId, Long userId, String email, String phoneNumber, Type type) {
		this.target = new Target(targetId, title);
		this.receiver = new Receiver(userId, email, phoneNumber);
		this.canceled = false;
		this.type = type;
	}

	public static Ajaja plan(String title, Long targetId, Long userId) {
		return new Ajaja(title, targetId, userId, null, null, Type.PLAN);
	}

	public static Ajaja footprint(String title, Long targetId, Long userId) {
		return new Ajaja(title, targetId, userId, null, null, Type.FOOTPRINT);
	}

	public static Ajaja defaultValue() {
		return new Ajaja(null, DEFAULT_TARGET_ID, DEFAULT_USER_ID, null, null, Type.DEFAULT);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		Ajaja ajaja = (Ajaja)obj;

		return Objects.equals(target.getTargetId(), ajaja.target.getTargetId())
			&& Objects.equals(receiver.getUserId(), ajaja.receiver.getUserId())
			&& type == ajaja.type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(target.getTargetId(), receiver.getUserId(), type);
	}

	public void switchStatus() {
		this.canceled = !canceled;
	}

	public boolean isEqualsDefault() {
		return this.equals(Ajaja.defaultValue());
	}

	public boolean isValidNumber() {
		return !Objects.equals(this.getPhoneNumber(), "01000000000");
	}

	public String getType() {
		return this.type.name();
	}

	public Long getUserId() {
		return receiver.getUserId();
	}

	public String getPhoneNumber() {
		return receiver.getPhoneNumber();
	}

	public String getEmail() {
		return receiver.getEmail();
	}

	public String getTitle() {
		return target.getTitle();
	}

	public Long getTargetId() {
		return target.getTargetId();
	}
}
