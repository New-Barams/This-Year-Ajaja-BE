package com.newbarams.ajaja.module.ajaja.domain;

import com.newbarams.ajaja.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "ajajas")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ajaja extends BaseEntity<Ajaja> {
	enum Type {
		PLAN,
		RETROSPECT, // 회고
	}

	@EmbeddedId
	private AjajaId ajajaId;

	private boolean isCanceled;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "target_type")
	private Type type;

	private Ajaja(Long targetId, Long userId, Type type) {
		this.ajajaId = new AjajaId(targetId, userId);
		this.isCanceled = false;
		this.type = type;
		this.validateSelf();
	}

	public static Ajaja plan(Long targetId, Long userId) {
		return new Ajaja(targetId, userId, Type.PLAN);
	}

	public static Ajaja retrospect(Long targetId, Long userId) {
		return new Ajaja(targetId, userId, Type.RETROSPECT);
	}

	public Long getUserId() {
		return ajajaId.getUserId();
	}

	public void switchStatus() {
		this.isCanceled = !isCanceled;
	}
}
