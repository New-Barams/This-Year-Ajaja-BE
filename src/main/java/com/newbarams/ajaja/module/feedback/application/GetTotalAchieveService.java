package com.newbarams.ajaja.module.feedback.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.repository.PlanQueryRepository;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetTotalAchieveService {
	private final PlanQueryRepository planQueryRepository;

	public int calculateTotalAchieve(Long userId) {
		List<PlanInfoResponse.GetPlan> planList = planQueryRepository.findAllPlanByUserId(userId);

		return (int)planList
			.stream()
			.mapToInt(PlanInfoResponse.GetPlan::achieveRate)
			.average()
			.orElse(0);
	}
}
