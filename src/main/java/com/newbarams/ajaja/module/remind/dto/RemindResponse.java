package com.newbarams.ajaja.module.remind.dto;

import java.util.List;

public sealed interface RemindResponse
	permits RemindResponse.CommonResponse, RemindResponse.SentRemindResponse, RemindResponse.FutureRemindResponse {

	record CommonResponse(
		String remindTime,
		int remindDate,
		int remindTerm,
		int remindTotalPeriod,
		boolean isRemindable,
		List<SentRemindResponse> sentRemindResponses,
		List<FutureRemindResponse> futureRemindResponses

	) implements RemindResponse {
	}

	record SentRemindResponse(
		Long feedbackId,
		String remindMessage,
		int remindMonth,
		int remindDate,
		int rate,
		boolean isFeedback,
		boolean isExpired,
		boolean isReminded,
		int endMonth,
		int endDate
	) implements RemindResponse {
	}

	record FutureRemindResponse(
		Long feedbackId,
		String remindMessage,
		int remindMonth,
		int remindDate,
		int rate,
		boolean isFeedback,
		boolean isExpired,
		boolean isReminded,
		int endMonth,
		int endDate
	) implements RemindResponse {
		public static FutureRemindResponse of(int remindMonth, int remindDate) {
			return new FutureRemindResponse(
				0L,
				"",
				remindMonth,
				remindDate,
				0,
				false,
				false,
				false,
				0,
				0
			);
		}
	}
}
