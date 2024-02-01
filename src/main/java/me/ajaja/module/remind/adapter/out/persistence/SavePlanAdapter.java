package me.ajaja.module.remind.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.PlanRepository;
import me.ajaja.module.remind.application.port.out.SavePlanPort;

@Repository
@RequiredArgsConstructor
public class SavePlanAdapter implements SavePlanPort {
	private final PlanRepository planRepository;

	@Override
	public void update(Plan plan) {
		planRepository.save(plan);
	}
}
