package me.ajaja.module.remind.application.port.out;

import me.ajaja.module.plan.domain.Plan;

public interface FindTargetPort {
	Plan findByUserIdAndPlanId(Long userId, Long planId);
}
