package com.newbarams.ajaja.module.feedback.domain;

import java.util.List;
import java.util.Optional;

import com.newbarams.ajaja.module.feedback.infra.model.AchieveInfo;
import com.newbarams.ajaja.module.feedback.infra.model.FeedbackInfo;

public interface FeedbackQueryRepository {
	List<Feedback> findAllFeedbackByPlanId(Long planId);

	Optional<Feedback> findByFeedbackId(Long feedbackId);

	List<FeedbackInfo> findFeedbackInfosByPlanId(Long planId);

	List<AchieveInfo> findAchievesByUserIdAndYear(Long userId, int year);
}
