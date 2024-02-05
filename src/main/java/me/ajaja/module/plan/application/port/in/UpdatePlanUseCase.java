package me.ajaja.module.plan.application.port.in;

import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;

public interface UpdatePlanUseCase {
	PlanResponse.Detail update(Long id, Long userId, PlanRequest.Update request, int month);
}
