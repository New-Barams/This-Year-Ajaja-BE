package com.newbarams.ajaja.module.plan.dto;

import java.util.List;

import lombok.Getter;

public sealed interface PlanInfo permits PlanInfo.PlanInfoResponse, PlanInfo.GetPlanInfo {
	record PlanInfoResponse(
		int totalAchieveRate,
		List<GetPlanInfo> getPlanInfoList
	) implements PlanInfo {
	}

	record GetPlanInfo(
		String title,
		boolean isRemindable,
		@Getter
		int achieveRate
	) implements PlanInfo {
	}
}
