package me.ajaja.module.remind.application.port.in;

import me.ajaja.module.plan.dto.PlanRequest;

public interface UpdateRemindInfoUseCase {
	void update(Long userId, Long planId, PlanRequest.UpdateRemind request);
}
