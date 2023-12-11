package com.newbarams.ajaja.module.feedback.domain;

import java.time.Instant;

import com.newbarams.ajaja.global.common.SelfValidating;
import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.exception.ErrorCode;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Feedback extends SelfValidating<Feedback> {
	private Long id;
	@NotNull
	private final Long userId;
	@NotNull
	private final Long planId;
	private final Achieve achieve;

	private Instant createdAt;
	private Instant updatedAt;

	public Feedback(Long id, Long userId, Long planId, Achieve achieve, Instant createdAt,
		Instant updatedAt) {
		this.id = id;
		this.userId = userId;
		this.planId = planId;
		this.achieve = achieve;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	Feedback(Long userId, Long planId) {
		this.id = null;
		this.userId = userId;
		this.planId = planId;
		this.achieve = Achieve.FAIL;
		this.createdAt = null;
		this.updatedAt = null;
		this.validateSelf();
	}

	public static Feedback create(Long userId, Long planId) {
		return new Feedback(userId, planId);
	}

	public int getRate() {
		return this.achieve.getRate();
	}

	public boolean isFeedback() {
		return this.getUpdatedAt().isAfter(this.getCreatedAt());
	}

	public String getAchieveName(int rate) {
		return Achieve.of(rate).name();
	}

	public void checkDeadline() {
		if (TimeValue.check(this.createdAt)) {
			throw new AjajaException(ErrorCode.EXPIRED_FEEDBACK);
		}
	}
}
