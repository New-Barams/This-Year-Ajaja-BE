package com.newbarams.ajaja.module.plan.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.plan.repository.PlanQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetPlanInfoService {
	private final PlanQueryRepository planQueryRepository;

	public PlanInfoResponse.GetPlanInfoResponse loadPlanInfo(Long userId) {
		List<PlanInfoResponse.GetGetPlan> planInfos = planQueryRepository.findAllPlanByUserId(userId);

		int totalAchieve = (int)planInfos
			.stream()
			.mapToInt(PlanInfoResponse.GetGetPlan::getAchieveRate)
			.average()
			.orElse(0);

		return new PlanInfoResponse.GetPlanInfoResponse(totalAchieve, planInfos);
	}
}
