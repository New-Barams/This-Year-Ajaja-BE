package me.ajaja.module.plan.application.port.in;

import me.ajaja.module.plan.dto.PlanResponse;

public interface LoadPlanDetailUseCase {
	PlanResponse.Detail loadByIdAndOptionalUser(Long userId, Long id);
}
