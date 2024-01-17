package com.newbarams.ajaja.module.remind.application.port.out;

import com.newbarams.ajaja.module.plan.domain.Plan;

public interface FindPlanPort {
	Plan findByUserIdAndPlanId(Long userId, Long planId);
}
