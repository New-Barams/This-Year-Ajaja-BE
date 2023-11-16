package com.newbarams.ajaja.module.remind.domain.dto;

import java.util.List;

public sealed interface GetRemindInfo
	permits GetRemindInfo.CommonResponse, GetRemindInfo.PastRemindResponse, GetRemindInfo.FutureRemindResponse {

	record CommonResponse(
		int remindTime,
		int remindDate,
		int remindTerm,
		int remindTotalPeriod,
		boolean isRemindable,
		List<PastRemindResponse> pastRemindResponses,
		List<FutureRemindResponse> futureRemindResponses

	) implements GetRemindInfo {
	}

	record PastRemindResponse(
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
	) implements GetRemindInfo {
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
	) implements GetRemindInfo {
	}
}
