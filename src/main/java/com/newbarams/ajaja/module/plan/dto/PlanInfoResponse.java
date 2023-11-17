package com.newbarams.ajaja.module.plan.dto;

import java.util.List;

import lombok.Getter;

public sealed interface PlanInfoResponse permits PlanInfoResponse.GetPlanInfoResponse, PlanInfoResponse.GetPlan {
	record GetPlanInfoResponse(
		int totalAchieveRate,
		List<GetPlan> getPlanList
	) implements PlanInfoResponse {
	}

	record GetPlan(
		String title,
		boolean isRemindable,
		@Getter
		int achieveRate
	) implements PlanInfoResponse {
	}
}
