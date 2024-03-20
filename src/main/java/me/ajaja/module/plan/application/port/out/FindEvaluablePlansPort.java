package me.ajaja.module.plan.application.port.out;

import java.util.List;

import me.ajaja.module.plan.domain.Plan;

public interface FindEvaluablePlansPort {
	List<Plan> findEvaluablePlansByUserId(Long id);
}
