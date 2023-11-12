package com.newbarams.ajaja.module.plan.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.dto.PlanInfo;
import com.newbarams.ajaja.module.plan.repository.PlanQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GetPlanInfoService {
	private final PlanQueryRepository planQueryRepository;

	public PlanInfo.PlanInfoResponse loadPlanInfo(Long userId) {
		List<PlanInfo.GetPlanInfo> planInfos = planQueryRepository.findAllPlanByUserId(userId);

		int totalAchieve = (int)planInfos
			.stream()
			.mapToInt(PlanInfo.GetPlanInfo::getAchieveRate)
			.average()
			.orElse(0);

		return new PlanInfo.PlanInfoResponse(totalAchieve, planInfos);
	}
}
