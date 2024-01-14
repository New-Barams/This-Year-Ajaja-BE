package com.newbarams.ajaja.module.remind.application.port.in;

import com.newbarams.ajaja.global.common.TimeValue;
import com.newbarams.ajaja.module.plan.domain.Plan;

public interface CreateRemindUseCase {
	void save(Plan plan, String message, TimeValue time);
}
