package com.newbarams.ajaja.module.feedback.domain;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.hibernate.annotations.Where;

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
@Table(name = "feedbacks")
@Where(clause = "is_deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseEntity<Feedback> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "feedback_id")
	private Long id;

	@NotNull
	private Long userId;

	@NotNull
	private Long planId;

	@Getter
	@Enumerated(value = EnumType.STRING)
	@NotNull
	private Achieve achieve;

	private boolean isDeleted;

	public Feedback(Long userId, Long planId, Achieve achieve) {
		this.userId = userId;
		this.planId = planId;
		this.achieve = achieve;
		this.isDeleted = false;
		this.validateSelf();
	}

	public void checkDeadline() throws IllegalAccessException {
		Instant remindDate = this.getCreatedAt();
		Timestamp currentDate = new Timestamp(System.currentTimeMillis());

		Timestamp deadline = Timestamp.from(remindDate.plus(31, ChronoUnit.DAYS));

		boolean isInvalidFeedback = currentDate.before(Timestamp.from(remindDate)) || currentDate.after(deadline);

		if (isInvalidFeedback) {
			throw new IllegalAccessException("평가 기간이 지났습니다.");
		}
	}

	public void updateAchieve(int rate) {
		this.achieve = Achieve.findByRate(rate);
	}
}
