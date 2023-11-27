package com.newbarams.ajaja.module.plan.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.repository.PlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPlanAchieveService {

	private final PlanRepository planRepository;

	public int calculatePlanAchieve(Long planId) {
		Plan plan = planRepository.findById(planId)
			.orElseThrow(() -> AjajaException.withId(planId, NOT_FOUND_PLAN));

		return plan.getAchieveRate();
	}
}
