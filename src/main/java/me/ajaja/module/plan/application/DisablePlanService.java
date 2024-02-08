package me.ajaja.module.plan.application;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.application.port.out.FindAllPlansQuery;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.user.application.port.out.DisablePlanPort;

@Component
@Transactional
@RequiredArgsConstructor
class DisablePlanService implements DisablePlanPort {
	private final FindAllPlansQuery findAllPlansQuery;

	@Override
	public void disable(Long userId) {
		for (Plan plan : findAllPlansQuery.findAllCurrentPlansByUserId(userId)) {
			plan.disable();
		}
	}
}
