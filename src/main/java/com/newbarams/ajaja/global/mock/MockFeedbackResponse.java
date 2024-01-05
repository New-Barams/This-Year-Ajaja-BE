package com.newbarams.ajaja.global.mock;

import java.util.List;

import lombok.Data;

public final class MockFeedbackResponse { // todo : 실제 API 생성 후 삭제
	@Data
	public static class FeedbackInfo {
		private final int achieveRate;
		private final String title;
		private final int remindTime;
		private final List<RemindedFeedback> feedbacks;
	}

	@Data
	public static class RemindedFeedback {
		private final int achieve;
		private final String message;
		private final int remindMonth;
		private final int remindDay;
		private final boolean reminded;
	}
}
