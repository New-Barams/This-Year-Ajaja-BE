package com.newbarams.ajaja.module.remind.dto;

import java.util.List;

public sealed interface RemindResponse
	permits RemindResponse.CommonResponse, RemindResponse.SentResponse, RemindResponse.FutureResponse {

	record CommonResponse(
		String remindTime,
		int remindDate,
		int remindTerm,
		int remindTotalPeriod,
		boolean isRemindable,
		List<SentResponse> sentRespons,
		List<FutureResponse> futureRespons

	) implements RemindResponse {
	}

	record SentResponse(
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

	record FutureResponse(
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
}
