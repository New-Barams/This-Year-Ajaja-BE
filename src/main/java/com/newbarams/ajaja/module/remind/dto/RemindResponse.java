package com.newbarams.ajaja.module.remind.dto;

import java.util.List;

public sealed interface RemindResponse
	permits RemindResponse.CommonResponse, RemindResponse.Response {

	record CommonResponse(
		String remindTime,
		boolean isRemindable,
		List<Response> RemindResponses

	) implements RemindResponse {
	}

	record Response(
		String remindMessage,
		int remindMonth,
		int remindDate,
		boolean isReminded
	) implements RemindResponse {
	}
}
