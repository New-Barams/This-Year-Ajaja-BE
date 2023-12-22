package com.newbarams.ajaja.module.plan.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.newbarams.ajaja.module.feedback.application.LoadTotalAchieveService;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreatePlanResponseService {
	private final LoadTotalAchieveService loadTotalAchieveService;

	PlanInfoResponse.GetPlanInfoResponse createPlanInfo(int planYear, List<PlanInfoResponse.GetPlan> planInfos,
		Long userId) {
		List<PlanInfoResponse.GetPlan> getPlans = planInfos.stream()
			.filter(plan -> plan.year() == planYear)
			.toList();

		int totalAchieve = loadTotalAchieveService.loadTotalAchieve(userId, planYear);

		return new PlanInfoResponse.GetPlanInfoResponse(planYear, totalAchieve, getPlans);
	}
}
