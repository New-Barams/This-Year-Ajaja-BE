package com.newbarams.ajaja.module.plan.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadPlanInfoService {
	private final PlanQueryRepository planQueryRepository;
	private final CreatePlanResponseService createPlanResponseService;

	public List<PlanInfoResponse.GetPlanInfoResponse> loadPlanInfo(Long userId) {
		List<PlanInfoResponse.GetPlan> planInfos = planQueryRepository.findAllPlanByUserId(userId);

		if (planInfos.isEmpty()) {
			return Collections.EMPTY_LIST;
		}

		int currentYear = planInfos.get(0).year();
		int firstYear = planInfos.get(planInfos.size() - 1).year();

		return loadPlanInfoResponses(currentYear, firstYear, planInfos);
	}

	private List<PlanInfoResponse.GetPlanInfoResponse> loadPlanInfoResponses(
		int currentYear,
		int firstYear,
		List<PlanInfoResponse.GetPlan> planInfos
	) {
		List<PlanInfoResponse.GetPlanInfoResponse> planInfoResponses = new ArrayList<>();

		for (int i = currentYear; i >= firstYear; i--) {
			PlanInfoResponse.GetPlanInfoResponse planInfoResponse
				= createPlanResponseService.createPlanInfo(i, planInfos);

			planInfoResponses.add(planInfoResponse);
		}

		return planInfoResponses;
	}
}
