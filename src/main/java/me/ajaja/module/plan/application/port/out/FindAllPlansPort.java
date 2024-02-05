package me.ajaja.module.plan.application.port.out;

import java.util.List;

import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;

public interface FindAllPlansPort {
	List<PlanResponse.GetAll> findAllByCursorAndSorting(PlanRequest.GetAll conditions);

	List<Plan> findAllCurrentPlansByUserId(Long userId);
}
