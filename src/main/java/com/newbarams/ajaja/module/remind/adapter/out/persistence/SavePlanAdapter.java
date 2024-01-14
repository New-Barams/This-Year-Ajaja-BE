package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import org.springframework.stereotype.Repository;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.remind.application.port.out.SavePlanPort;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SavePlanAdapter implements SavePlanPort {
	private final PlanRepository planRepository;

	@Override
	public void save(Plan plan) {
		planRepository.save(plan);
	}
}
