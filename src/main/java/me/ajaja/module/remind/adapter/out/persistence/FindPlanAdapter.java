package me.ajaja.module.remind.adapter.out.persistence;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.application.LoadPlanService;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.remind.application.port.out.FindPlanPort;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindPlanAdapter implements FindPlanPort {
	private final LoadPlanService loadPlanService;

	@Override
	public Plan findByUserIdAndPlanId(Long userId, Long planId) {
		return loadPlanService.loadByUserIdAndPlanId(userId, planId);
	}
}
