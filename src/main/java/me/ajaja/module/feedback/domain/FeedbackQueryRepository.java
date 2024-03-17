package me.ajaja.module.feedback.domain;

import java.util.List;

import me.ajaja.global.common.BaseTime;
import me.ajaja.module.feedback.infra.model.AchieveInfo;
import me.ajaja.module.feedback.infra.model.FeedbackInfo;

public interface FeedbackQueryRepository {
	List<Feedback> findAllFeedbackByPlanId(Long planId);

	boolean existByPlanIdAndPeriod(Long planId, BaseTime period);

	List<FeedbackInfo> findFeedbackInfosByPlanId(Long planId);

	List<AchieveInfo> findAchievesByUserIdAndYear(Long userId, int year);
}
