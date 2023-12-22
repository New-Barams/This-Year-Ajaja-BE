package com.newbarams.ajaja.module.feedback.dto;

import java.util.List;

import lombok.Data;

public class FeedbackResponse {
	@Data
	public static class FeedbackInfo {
		int achieveRate;
		String planName;
		int totalPeriod;
		int remindTerm;
		int remindDay;
		List<RemindedFeedback> feedbacks;
	}

	@Data
	public static class RemindedFeedback {
		Long feedbackId;
		int achieve;
		int remindMonth;
		int remindDay;
		boolean feedbacked;
	}
}
