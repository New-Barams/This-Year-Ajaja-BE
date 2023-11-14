package com.newbarams.ajaja.global.mock;

import java.util.List;

public sealed interface MockPlanInfoResponse permits MockPlanInfoResponse.GetPlanInfoResponse,
	MockPlanInfoResponse.GetPlan {
	record GetPlanInfoResponse(
		int totalAchieveRate,
		List<GetPlan> getPlanList
	) implements MockPlanInfoResponse {
	}

	record GetPlan(
		String title,
		boolean isRemindable,
		int achieveRate,
		int icon
	) implements MockPlanInfoResponse {
	}
}
