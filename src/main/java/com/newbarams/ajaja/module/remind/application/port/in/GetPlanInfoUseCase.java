package com.newbarams.ajaja.module.remind.application.port.in;

import java.util.List;

import com.newbarams.ajaja.module.plan.dto.PlanResponse;

public interface GetPlanInfoUseCase {
	List<PlanResponse.MainInfo> load(Long userId);
}
