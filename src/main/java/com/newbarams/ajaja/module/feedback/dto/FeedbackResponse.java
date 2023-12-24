package com.newbarams.ajaja.module.feedback.dto;

import java.util.List;

import lombok.Data;

public final class FeedbackResponse {
	@Data
	public static class FeedbackInfo {
		private final int achieveRate;
		private final String planName;
		private final int totalPeriod;
		private final int remindTerm;
		private final int remindDay;
		private final List<RemindedFeedback> feedbacks;
	}

	@Data
	public static class RemindedFeedback {
		private final Long feedbackId;
		private final int achieve;
		private final String message;
		private final int remindMonth;
		private final int remindDay;
		private final boolean feedbacked;
	}
}
