package me.ajaja.module.remind.application.port.out;

import me.ajaja.module.plan.domain.Plan;

public interface FindRemindInfoPort {
	Plan loadByUserIdAndPlanId(Long userId, Long id);
}
