package com.newbarams.ajaja.module.plan.dto;

import java.util.List;

public sealed interface PlanInfoResponse permits PlanInfoResponse.GetPlanInfoResponse, PlanInfoResponse.GetPlan {
	record GetPlanInfoResponse( // todo : dto 다시 분류해보기 , 네이밍 수정
								int year,
								int totalAchieveRate,
								List<GetPlan> getPlanList
	) implements PlanInfoResponse {
	}

	record GetPlan(
		int year,
		Long planId,
		String title,
		boolean isRemindable,
		int achieveRate,
		int icon,
		boolean isVerified
	) implements PlanInfoResponse {
	}
}
