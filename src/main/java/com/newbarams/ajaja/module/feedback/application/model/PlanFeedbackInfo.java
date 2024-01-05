package com.newbarams.ajaja.module.feedback.application.model;

import java.util.List;

import com.newbarams.ajaja.module.plan.domain.Message;

public record PlanFeedbackInfo(
	int createdYear,
	int remindMonth,
	int remindDate,
	int totalPeriod,
	int remindTerm,
	int remindTime,
	String title,
	List<Message> messages
) {
}
