package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.application.port.out.FindPlanPort;

import lombok.RequiredArgsConstructor;

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
