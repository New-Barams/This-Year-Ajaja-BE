package me.ajaja.module.remind.application.port.out;

import me.ajaja.module.remind.dto.RemindResponse;

public interface FindTargetRemindQuery {
	RemindResponse.RemindInfo findByUserIdAndPlanId(Long userId, Long planId);
}
