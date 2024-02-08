package me.ajaja.module.plan.application.port.out;

import java.util.Optional;

import me.ajaja.module.plan.domain.Plan;

public interface FindPlanPort {
	Optional<Plan> findById(Long id);
}
