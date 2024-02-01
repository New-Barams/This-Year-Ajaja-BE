package me.ajaja.module.remind.application.port.out;

import java.util.List;

import me.ajaja.module.plan.dto.PlanResponse;

public interface FindPlanInfoPort {
	List<PlanResponse.PlanInfo> findAllPlanInfosByUserId(Long userId);
}
