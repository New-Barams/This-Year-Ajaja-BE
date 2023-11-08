package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.repository.PlanQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPlanService {
	private final PlanQueryRepository planQueryRepository;

	public PlanResponse.GetOne loadById(Long id) {
		return planQueryRepository.findPlanById(id);
	}
}
