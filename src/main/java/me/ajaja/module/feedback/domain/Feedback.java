package me.ajaja.module.feedback.domain;

import static me.ajaja.global.exception.ErrorCode.*;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;
import me.ajaja.global.common.TimeValue;
import me.ajaja.global.exception.AjajaException;

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

	public static Feedback create(Long userId, Long planId, int rate, String message) {
		return new Feedback(null, userId, planId, Achieve.of(rate), message, null, null);
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
