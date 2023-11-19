package com.newbarams.ajaja.module.remind.domain.dto;

import java.util.Collections;
import java.util.List;

import com.newbarams.ajaja.module.plan.domain.Plan;

public sealed interface GetRemindInfo
	permits GetRemindInfo.CommonResponse, GetRemindInfo.SentRemindResponse, GetRemindInfo.FutureRemindResponse {

	record CommonResponse(
		int remindTime,
		int remindDate,
		int remindTerm,
		int remindTotalPeriod,
		boolean isRemindable,
		List<SentRemindResponse> sentRemindResponses,
		List<FutureRemindResponse> futureRemindResponses

	) implements GetRemindInfo {
		public CommonResponse(Plan plan, List<FutureRemindResponse> futureRemindResponse) {
			this(
				plan.getRemindTime(),
				plan.getRemindDate(),
				plan.getRemindTerm(),
				plan.getRemindTotalPeriod(),
				plan.getIsRemindable(),
				Collections.emptyList(),
				futureRemindResponse
			);
		}
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
		public FutureRemindResponse(String remindMessage, int remindMonth, int remindDate) {
			this(
				0L,
				remindMessage,
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
