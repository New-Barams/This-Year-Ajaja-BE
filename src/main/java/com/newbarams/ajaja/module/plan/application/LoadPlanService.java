package com.newbarams.ajaja.module.plan.application;

import static com.newbarams.ajaja.global.common.error.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.repository.PlanQueryRepository;
import com.newbarams.ajaja.module.plan.domain.repository.PlanRepository;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadPlanService {
	private final PlanRepository planRepository;
	private final PlanQueryRepository planQueryRepository;

	public PlanResponse.GetOne loadById(Long id, Long userId) {
		return planQueryRepository.findById(id, userId);
	}

	public Plan loadPlanOrElseThrow(Long id) {
		return planRepository.findById(id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_PLAN));
	}

	public List<PlanResponse.GetAll> loadAllPlans(PlanRequest.GetAll request) {
		return planQueryRepository.findAllByCursorAndSorting(request);
	}
}
