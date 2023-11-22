package com.newbarams.ajaja.module.plan.application;

import static com.newbarams.ajaja.global.common.error.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.common.exception.AjajaException;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.repository.PlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdatePlanAchieveService {
	private final PlanRepository planRepository;

	public void updatePlanAchieve(Long planId, int feedbackAverage) {
		Plan plan = planRepository.findById(planId)
			.orElseThrow(() -> AjajaException.withId(planId, NOT_FOUND_PLAN));

		plan.updateAchieve(feedbackAverage);
	}
}
