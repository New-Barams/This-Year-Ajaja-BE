package me.ajaja.module.remind.application.port.in;

import java.util.List;

import me.ajaja.module.plan.dto.PlanResponse;

public interface GetPlanInfoUseCase {
	List<PlanResponse.MainInfo> load(Long userId);
}
