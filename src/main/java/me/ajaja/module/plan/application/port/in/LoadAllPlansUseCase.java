package me.ajaja.module.plan.application.port.in;

import java.util.List;

import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;

public interface LoadAllPlansUseCase {
	List<PlanResponse.GetAll> loadAllPlans(PlanRequest.GetAll request);
}
