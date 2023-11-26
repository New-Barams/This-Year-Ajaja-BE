package com.newbarams.ajaja.module.remind.dto;

import java.util.Collections;
import java.util.List;

import com.newbarams.ajaja.module.feedback.domain.Feedback;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.Remind;

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

		public static CommonResponse of(
			Plan plan,
			List<SentRemindResponse> sentRemindResponses,
			List<FutureRemindResponse> futureRemindResponse
		) {
			return new CommonResponse(
				plan.getRemindTimeName(),
				plan.getRemindDate(),
				plan.getRemindTerm(),
				plan.getRemindTotalPeriod(),
				plan.getIsRemindable(),
				sentRemindResponses,
				futureRemindResponse
			);
		}

		public static CommonResponse of(Plan plan, List<FutureRemindResponse> futureRemindResponse) {
			return new CommonResponse(
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
	) implements RemindResponse {
		public static SentRemindResponse of(
			Remind remind,
			Feedback feedback,
			int remindMonth,
			int remindDate,
			boolean isFeedback,
			boolean isExpired,
			int endMonth,
			int endDate
		) {
			return new SentRemindResponse(
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
	) implements RemindResponse {
		public static FutureRemindResponse of(String remindMessage, int remindMonth, int remindDate) {
			return new FutureRemindResponse(
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
