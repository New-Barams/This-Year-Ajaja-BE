package me.ajaja.module.feedback.application.model;

import java.util.List;

public record PlanFeedbackInfo(
	int createdYear,
	int remindMonth,
	int remindDate,
	int totalPeriod,
	int remindTerm,
	int remindTime,
	String title,
	List<FeedbackPeriod> periods
) {
}
