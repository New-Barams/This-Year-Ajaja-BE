package com.newbarams.ajaja.module.feedback.domain;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import com.newbarams.ajaja.global.common.SelfValidating;
import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.global.exception.AjajaException;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Feedback extends SelfValidating<Feedback> {
	private Long id;
	@NotNull
	private final Long userId;
	@NotNull
	private final Long planId;
	private Info info;

	private final TimeValue createdAt;
	private final TimeValue updatedAt;

	public Feedback(Long id, Long userId, Long planId, Achieve achieve, String message, TimeValue createdAt,
		TimeValue updatedAt) {
		this.id = id;
		this.userId = userId;
		this.planId = planId;
		this.info = new Info(achieve, message);
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.validateSelf();
	}

	public static Feedback create(Long userId, Long planId) {
		return new Feedback(null, userId, planId, Achieve.FAIL, "", null, null);
	}

	public boolean isFeedback() {
		return this.getUpdatedAt().isAfter(this.getCreatedAt());
	}

	public void updateFeedback(int rate, String message) {
		if (createdAt.isExpired()) {
			throw new AjajaException(EXPIRED_FEEDBACK);
		}

		this.info = new Info(Achieve.of(rate), message);
	}
}
