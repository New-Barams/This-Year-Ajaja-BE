package com.newbarams.ajaja.module.plan.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;

@Service
public class CreatePlanResponseService {

	PlanInfoResponse.GetPlanInfoResponse createPlanInfo(int planYear, List<PlanInfoResponse.GetPlan> planInfos) {
		List<PlanInfoResponse.GetPlan> getPlans = planInfos.stream()
			.filter(plan -> plan.year() == planYear)
			.toList();

		int totalAchieve = (int)getPlans
			.stream()
			.mapToInt(PlanInfoResponse.GetPlan::achieveRate)
			.average()
			.orElse(0);

		return new PlanInfoResponse.GetPlanInfoResponse(planYear, totalAchieve, getPlans);
	}
}
