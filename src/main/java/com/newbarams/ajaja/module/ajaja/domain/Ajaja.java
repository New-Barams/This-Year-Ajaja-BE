package com.newbarams.ajaja.module.ajaja.domain;

import com.newbarams.ajaja.global.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ajaja_id")
	private Long id;

	@NotNull
	@Column(name = "target_id")
	private Long targetId;

	@NotNull
	private Long userId;

	private boolean isCanceled;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "target_type")
	private Type type;

	private Ajaja(Long targetId, Long userId, Type type) {
		this.targetId = targetId;
		this.userId = userId;
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
		return userId;
	}

	public void switchStatus() {
		this.isCanceled = !isCanceled;
	}
}
