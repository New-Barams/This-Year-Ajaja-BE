package me.ajaja.module.feedback.infra;

import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ajaja.global.common.BaseEntity;
import me.ajaja.module.feedback.domain.Feedback;

@Entity
@Getter
@Table(name = "feedbacks")
@Where(clause = "deleted = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FeedbackEntity extends BaseEntity<Feedback> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, name = "feedback_id")
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private Long planId;

	@Column(nullable = false)
	private int achieve;

	@Column(nullable = false, length = 100, name = "feedback_message")
	private String message;

	@Column(nullable = false)
	private boolean deleted;
}
