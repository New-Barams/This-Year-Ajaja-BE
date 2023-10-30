package com.newbarams.ajaja.module.ajaja;

import java.time.Instant;

import org.hibernate.annotations.Where;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@Where(clause = "isCanceled = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ajaja extends SelfValidating<Ajaja> {
	enum Type {
		PLAN,
		RETROSPECT, // 회고
	}

	private boolean isCanceled;

	@PastOrPresent
	private Instant createdAt;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "target_type")
	private Type type;

	Ajaja(boolean isCanceled, Instant createdAt, Type type) {
		this.isCanceled = isCanceled;
		this.createdAt = createdAt;
		this.type = type;
		this.validateSelf();
	}

	public static Ajaja plan() {
		return new Ajaja(false, Instant.now(), Type.PLAN);
	}

	public static Ajaja retrospect() {
		return new Ajaja(false, Instant.now(), Type.RETROSPECT);
	}
}
