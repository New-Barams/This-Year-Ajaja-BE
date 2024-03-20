package me.ajaja.module.feedback.dto;

import java.util.List;

import lombok.Data;

public final class FeedbackResponse {
	@Data
	public static class FeedbackInfo {
		private final int createdYear;
		private final int achieveRate;
		private final String title;
		private final int remindTime;
		private final List<RemindFeedback> feedbacks;
	}

	@Data
	public static class RemindFeedback {
		private final int achieve;
		private final String message;
		private final int remindMonth;
		private final int remindDate;
		private final int endMonth;
		private final int endDate;
		private final boolean reminded;
	}

	@Data
	public static class EvaluableFeedback {
		private final String title;
		private final Long planId;
		private final long remainPeriod;
		private final int month;
		private final int date;
	}
}
