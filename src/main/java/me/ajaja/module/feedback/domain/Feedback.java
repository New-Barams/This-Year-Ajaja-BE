package me.ajaja.module.feedback.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import me.ajaja.global.common.BaseTime;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Feedback extends SelfValidating<Feedback> {
	private Long id;

	@NotNull
	private final Long userId;

	@NotNull
	private final Long planId;

	private Info info;

	private final BaseTime createdAt;
	private final BaseTime updatedAt;

	public Feedback(Long id, Long userId, Long planId, Achieve achieve, String message, BaseTime createdAt,
		BaseTime updatedAt) {
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
}
