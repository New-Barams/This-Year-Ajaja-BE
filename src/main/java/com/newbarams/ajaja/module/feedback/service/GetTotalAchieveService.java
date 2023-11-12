package com.newbarams.ajaja.module.feedback.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.dto.PlanInfo;
import com.newbarams.ajaja.module.plan.repository.PlanQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetTotalAchieveService {
	private final PlanQueryRepository planQueryRepository;

	public int calculateTotalAchieve(Long userId) {
		List<PlanInfo.GetPlanInfo> planList = planQueryRepository.findAllPlanByUserId(userId);

		return (int)planList
			.stream()
			.mapToInt(PlanInfo.GetPlanInfo::getAchieveRate)
			.average()
			.orElse(0);
	}
}
