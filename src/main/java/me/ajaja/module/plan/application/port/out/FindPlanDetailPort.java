package me.ajaja.module.plan.application.port.out;

import java.util.Optional;

import me.ajaja.module.plan.dto.PlanResponse;

public interface FindPlanDetailPort {
	Optional<PlanResponse.Detail> findPlanDetailByIdAndOptionalUser(Long userId, Long id);
}
