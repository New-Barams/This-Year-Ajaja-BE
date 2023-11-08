package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.repository.PlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPlanService {
	private final PlanRepository planRepository;

	public PlanResponse.GetOne loadById(Long id) {
		return planRepository.findPlanById(id);
	}
}
