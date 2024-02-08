package me.ajaja.module.plan.application.port.out;

import java.util.List;

import me.ajaja.module.plan.domain.Plan;

public interface SavePlanPort {
	Plan save(Plan plan);

	List<Plan> saveAll(List<Plan> plans);
}
