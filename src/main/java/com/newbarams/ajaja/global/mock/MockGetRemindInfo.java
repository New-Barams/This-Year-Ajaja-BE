package com.newbarams.ajaja.global.mock;

import java.sql.Timestamp;
import java.util.List;

public sealed interface MockGetRemindInfo permits MockGetRemindInfo.CommonResponse, MockGetRemindInfo.Response {

	record CommonResponse(
		String remindTime,
		int remindDate,
		int remindTerm,
		int remindTotalPeriod,
		boolean isRemindable,
		List<Response> responses) implements MockGetRemindInfo {
	}

	record Response(
		int index,
		Long feedbackId,
		String remindMessage,
		boolean isFeedback,
		int rate,
		boolean isExpired,
		Timestamp deadLine
	) implements MockGetRemindInfo {
	}
}
