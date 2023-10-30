package com.newbarams.ajaja.module.remind.domain;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reminds")
@Where(clause = "isDeleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Remind extends BaseEntity<Remind> {
	enum Type {
		PLAN,
		AJAJA
	}

	@NotNull
	private Long userId;

	@NotNull
	private Long planId;

	@Embedded
	private Info info;
	private Period period;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "remind_type")
	private Type type;

	private boolean isDeleted;

	Remind(Long userId, Long planId, Info info, Period period, Type type, boolean isDeleted) {
		this.userId = userId;
		this.planId = planId;
		this.info = info;
		this.period = period;
		this.type = type;
		this.isDeleted = isDeleted;
	}

	public static Remind plan(Long userId, Long planId, Info info, Period period) {
		return new Remind(userId, planId, info, period, Type.PLAN, false);
	}

	public static Remind ajaja(Long userId, Long planId, Info info, Period period) {
		return new Remind(userId, planId, info, period, Type.AJAJA, false);
	}
}
