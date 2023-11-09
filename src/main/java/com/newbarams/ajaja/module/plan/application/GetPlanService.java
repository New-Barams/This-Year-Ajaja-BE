package com.newbarams.ajaja.module.plan.application;

import static com.newbarams.ajaja.module.plan.exception.ErrorMessage.*;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.repository.PlanQueryRepository;
import com.newbarams.ajaja.module.plan.repository.PlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPlanService {
	private final PlanRepository planRepository;
	private final PlanQueryRepository planQueryRepository;

	public PlanResponse.GetOne loadById(Long id) {
		return planQueryRepository.findPlanById(id);
	}

	public Plan loadPlanOrElseThrow(Long id) {
		return planRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_PLAN.getMessage()));
	}
}
