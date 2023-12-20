package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.repository.PlanQueryRepository;
import com.newbarams.ajaja.module.user.application.port.out.DisablePlanPort;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
class DisablePlanService implements DisablePlanPort {
	private final PlanQueryRepository planQueryRepository;

	@Override
	public void disable(Long userId) {
		for (Plan plan : planQueryRepository.findAllCurrentPlansByUserId(userId)) {
			plan.disable();
		}
	}
}
