package com.newbarams.ajaja.module.feedback.domain;

import java.util.List;
import java.util.Optional;

public interface FeedbackQueryRepository {
	List<Feedback> findAllFeedbackByPlanId(Long planId);

	Optional<Feedback> findByFeedbackId(Long feedbackId);

	void update(Long feedbackId, String achieve);
}
