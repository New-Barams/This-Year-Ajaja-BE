package com.newbarams.ajaja.module.feedback.infra.model;

public record FeedbackInfo(
	Long feedbackId,
	boolean feedbacked,
	int achieve,
	String message,
	int remindMonth,
	int remindDay
) {
}
