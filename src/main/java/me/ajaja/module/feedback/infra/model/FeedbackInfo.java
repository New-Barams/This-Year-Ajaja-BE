package me.ajaja.module.feedback.infra.model;

public record FeedbackInfo(
	Long feedbackId,
	boolean feedbacked,
	int achieve,
	String message,
	int feedbackMonth,
	int feedbackDate
) {
}
