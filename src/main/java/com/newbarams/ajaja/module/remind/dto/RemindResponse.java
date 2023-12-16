package com.newbarams.ajaja.module.remind.dto;

import java.util.List;

public sealed interface RemindResponse
	permits RemindResponse.CommonResponse, RemindResponse.Messages {

	record CommonResponse(
		String remindTime,
		boolean isRemindable,
		List<Messages> messagesResponses
	) implements RemindResponse {
	}

	record Messages(
		String remindMessage,
		int remindMonth,
		int remindDate,
		boolean isReminded
	) implements RemindResponse {
	}
}
