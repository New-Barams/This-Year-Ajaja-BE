package com.newbarams.ajaja.module.feedback.domain;

import java.time.Instant;

import com.newbarams.ajaja.global.common.SelfValidating;
import com.newbarams.ajaja.global.common.TimeValue;

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

	private final Instant createdAt;
	private Instant updatedAt;

	public Feedback(Long id, Long userId, Long planId, Achieve achieve, Instant createdAt,
		Instant updatedAt) {
		this.id = id;
		this.userId = userId;
		this.planId = planId;
		this.achieve = achieve;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.validateSelf();
	}

	public static Feedback create(Long userId, Long planId) {
		return new Feedback(null, userId, planId, Achieve.FAIL, null, null);
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

	public boolean checkDeadline() {
		return TimeValue.check(this.createdAt);
	}
}
