package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.remind.application.port.out.FindPlanInfoPort;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FindPlanInfoAdapter implements FindPlanInfoPort {
	private final PlanQueryRepository planQueryRepository;

	@Override
	public List<PlanResponse.PlanInfo> findAllPlanByUserId(Long userId) {
		return planQueryRepository.findAllPlanByUserId(userId);
	}
}
