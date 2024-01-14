package com.newbarams.ajaja.module.remind.application.port.out;

import java.util.List;

import com.newbarams.ajaja.module.plan.dto.PlanResponse;

public interface FindPlanInfoPort {
	List<PlanResponse.PlanInfo> findAllPlanByUserId(Long userId);
}
