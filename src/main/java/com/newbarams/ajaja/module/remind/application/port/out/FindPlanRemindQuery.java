package com.newbarams.ajaja.module.remind.application.port.out;

import com.newbarams.ajaja.module.remind.dto.RemindResponse;

public interface FindPlanRemindQuery {
	RemindResponse.RemindInfo findByUserIdAndPlanId(Long userId, Long planId);
}
