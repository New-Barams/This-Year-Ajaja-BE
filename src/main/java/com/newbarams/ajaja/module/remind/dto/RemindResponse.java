package com.newbarams.ajaja.module.remind.dto;

import java.util.List;

public sealed interface RemindResponse
	permits RemindResponse.CommonResponse, RemindResponse.Messages {

	record CommonResponse(
		String remindTime,
		boolean isRemindable,
		int totalPeriod,
		int remindTerm,
		int remindDate,
		List<Messages> messagesResponses
	) implements RemindResponse {
	}

	record Messages(
		String remindMessage,
		int remindMonth,
		int remindDay,
		boolean isReminded
	) implements RemindResponse {
	}
}
