package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.user.application.service.DisablePlanService;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
class DisablePlanServiceImpl implements DisablePlanService {
	private final PlanQueryRepository planQueryRepository;

	@Override
	public void disable(Long userId) {
		for (Plan plan : planQueryRepository.findAllCurrentPlansByUserId(userId)) {
			plan.disable();
		}
	}
}
