package com.newbarams.ajaja.module.plan.dto;

import java.util.List;

import lombok.Getter;

public sealed interface PlanInfoResponse permits PlanInfoResponse.GetPlanInfoResponse, PlanInfoResponse.GetGetPlan {
	record GetPlanInfoResponse(
		int totalAchieveRate,
		List<GetGetPlan> getPlanList
	) implements PlanInfoResponse {
	}

	record GetGetPlan(
		String title,
		boolean isRemindable,
		@Getter
		int achieveRate
	) implements PlanInfoResponse {
	}
}
