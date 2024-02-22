package me.ajaja.module.plan.application.port.in;

import me.ajaja.module.plan.dto.PlanRequest;

public interface CreatePlanUseCase {
	Long create(Long userId, PlanRequest.Create request, int month);
}
