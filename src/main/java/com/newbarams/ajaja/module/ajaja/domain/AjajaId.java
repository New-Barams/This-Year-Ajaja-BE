package com.newbarams.ajaja.module.ajaja.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class AjajaId implements Serializable {
	@Column(name = "target_id")
	private Long targetId;

	@Column(name = "user_id")
	private Long userId;

	public AjajaId(Long targetId, Long userId) {
		this.targetId = targetId;
		this.userId = userId;
	}
}
