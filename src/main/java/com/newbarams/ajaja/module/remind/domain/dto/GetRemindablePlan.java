package com.newbarams.ajaja.module.remind.domain.dto;

import com.newbarams.ajaja.module.plan.domain.RemindInfo;

public sealed interface GetRemindablePlan permits GetRemindablePlan.Response {
	record Response(
		Long userId,
		Long planId,
		String email,
		String message,
		RemindInfo info
	) implements GetRemindablePlan {
	}
}
