package com.newbarams.ajaja.module.remind.dto;

import java.util.Collections;
import java.util.List;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.Remind;

public sealed interface GetRemindInfo
	permits GetRemindInfo.CommonResponse, GetRemindInfo.SentRemindResponse, GetRemindInfo.FutureRemindResponse {

	record CommonResponse(
		String remindTime,
		int remindDate,
		int remindTerm,
		int remindTotalPeriod,
		boolean isRemindable,
		List<SentRemindResponse> sentRemindResponses,
		List<FutureRemindResponse> futureRemindResponses

	) implements GetRemindInfo {
		public CommonResponse(Plan plan,
			List<SentRemindResponse> sentRemindResponses,
			List<FutureRemindResponse> futureRemindResponse
		) {
			this(
				plan.getRemindTimeName(),
				plan.getRemindDate(),
				plan.getRemindTerm(),
				plan.getRemindTotalPeriod(),
				plan.getIsRemindable(),
				sentRemindResponses,
				futureRemindResponse
			);
		}

		public CommonResponse(Plan plan) {
			this(
				plan.getRemindTimeName(),
				plan.getRemindDate(),
				plan.getRemindTerm(),
				plan.getRemindTotalPeriod(),
				plan.getIsRemindable(),
				Collections.emptyList(),
				Collections.emptyList()
			);
		}

		public CommonResponse(Plan plan, List<FutureRemindResponse> futureRemindResponse) {
			this(
				plan.getRemindTimeName(),
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
		public SentRemindResponse(
			Remind remind,
			Feedback feedback,
			int remindMonth,
			int remindDate,
			boolean isFeedback,
			boolean isExpired,
			int endMonth,
			int endDate
		) {
			this(
				feedback.getId(),
				remind.getInfo().getContent(),
				remindMonth,
				remindDate,
				feedback.getRate(),
				isFeedback,
				isExpired,
				true,
				endMonth,
				endDate
			);
		}
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

		public FutureRemindResponse(int remindMonth, int remindDate) {
			this(
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
