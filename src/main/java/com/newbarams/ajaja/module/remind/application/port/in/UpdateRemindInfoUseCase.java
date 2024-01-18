package com.newbarams.ajaja.module.remind.application.port.in;

import com.newbarams.ajaja.module.plan.dto.PlanRequest;

public interface UpdateRemindInfoUseCase {
	void update(Long userId, Long planId, PlanRequest.UpdateRemind request);
}
