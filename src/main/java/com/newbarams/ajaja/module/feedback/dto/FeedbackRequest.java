package com.newbarams.ajaja.module.feedback.dto;

import lombok.Data;

public final class FeedbackRequest {
	@Data
	public static class UpdateFeedback {
		private final int rate;
		private final String message;
	}
}
