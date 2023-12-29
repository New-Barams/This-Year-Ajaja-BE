package com.newbarams.ajaja.module.ajaja.domain;

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
		RETROSPECT, // 회고
		DEFAULT
	}

	private Long id;

	private Long targetId;

	private Long userId;

	private boolean canceled;

	private Type type;

	private Ajaja(Long targetId, Long userId, Type type) {
		this.targetId = targetId;
		this.userId = userId;
		this.canceled = false;
		this.type = type;
	}

	public static Ajaja plan(Long targetId, Long userId) {
		return new Ajaja(targetId, userId, Type.PLAN);
	}

	public static Ajaja retrospect(Long targetId, Long userId) {
		return new Ajaja(targetId, userId, Type.RETROSPECT);
	}

	public static Ajaja defaultValue() {
		return new Ajaja(DEFAULT_TARGET_ID, DEFAULT_USER_ID, Type.DEFAULT);
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

		return Objects.equals(targetId, ajaja.targetId) && Objects.equals(userId, ajaja.userId)
			&& type == ajaja.type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(targetId, userId, type);
	}

	public void switchStatus() {
		this.canceled = !canceled;
	}

	public boolean isEqualsDefault() {
		return this.equals(Ajaja.defaultValue());
	}

	public String getType() {
		return this.type.name();
	}
}
