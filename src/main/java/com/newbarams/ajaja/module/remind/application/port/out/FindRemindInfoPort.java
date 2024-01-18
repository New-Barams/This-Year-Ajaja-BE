package com.newbarams.ajaja.module.remind.application.port.out;

import com.newbarams.ajaja.module.plan.domain.Plan;

public interface FindRemindInfoPort {
	Plan loadByUserIdAndPlanId(Long userId, Long id);
}
