package com.newbarams.ajaja.module.feedback.domain;

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

	@Enumerated(value = EnumType.STRING)
	private Achieve achieve;

	private boolean isDeleted;

	public Feedback(Long userId, Long planId, Achieve achieve) {
		this.userId = userId;
		this.planId = planId;
		this.achieve = achieve;
		this.isDeleted = false;
		this.validateSelf();
	}
}
