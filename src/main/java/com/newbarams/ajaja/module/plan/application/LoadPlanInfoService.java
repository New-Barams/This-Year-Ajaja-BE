package com.newbarams.ajaja.module.plan.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.feedback.application.LoadTotalAchieveService;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadPlanInfoService {
	private final PlanQueryRepository planQueryRepository;
	private final LoadTotalAchieveService loadTotalAchieveService;
	private final PlanMapper mapper;

	public List<PlanInfoResponse.GetPlanInfoResponse> loadPlanInfo(Long userId) {
		List<PlanInfoResponse.GetPlan> planInfos = planQueryRepository.findAllPlanByUserId(userId);

		if (planInfos.isEmpty()) {
			return Collections.emptyList();
		}

		int currentYear = planInfos.get(0).year();
		int firstYear = planInfos.get(planInfos.size() - 1).year();

		return loadPlanInfoResponses(currentYear, firstYear, planInfos, userId);
	}

	private List<PlanInfoResponse.GetPlanInfoResponse> loadPlanInfoResponses(
		int currentYear,
		int firstYear,
		List<PlanInfoResponse.GetPlan> planInfos,
		Long userId
	) {
		List<PlanInfoResponse.GetPlanInfoResponse> planInfoResponses = new ArrayList<>();

		for (int planYear = currentYear; planYear >= firstYear; planYear--) {
			int totalAchieve = loadTotalAchieveService.loadTotalAchieve(userId, planYear);
			PlanInfoResponse.GetPlanInfoResponse planInfoResponse
				= mapper.toResponse(planYear, totalAchieve, planInfos);
			planInfoResponses.add(planInfoResponse);
		}

		return planInfoResponses;
	}
}
