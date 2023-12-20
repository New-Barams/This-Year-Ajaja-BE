package com.newbarams.ajaja.module.plan.application;

import static com.newbarams.ajaja.global.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.tag.application.CreatePlanTagService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreatePlanService {
	private static final int MAX_NUMBER_OF_PLANS = 4;

	private final PlanRepository planRepository;
	private final PlanQueryRepository planQueryRepository;
	private final CreatePlanTagService createPlanTagService;
	private final PlanMapper planMapper;

	public PlanResponse.Create create(Long userId, PlanRequest.Create request, int month) {
		checkNumberOfUserPlans(userId);

		Plan plan = Plan.create(planMapper.toParam(userId, request, month));
		Plan savedPlan = planRepository.save(plan);

		List<String> tags = createPlanTagService.create(savedPlan.getId(), request.tags());

		return planMapper.toResponse(savedPlan, tags);
	}

	private void checkNumberOfUserPlans(Long userId) {
		long countOfPlans = planQueryRepository.countByUserId(userId);

		if (isMoreThanMaxNumberOfPlans(countOfPlans)) {
			throw new AjajaException(EXCEED_MAX_NUMBER_OF_PLANS);
		}
	}

	private boolean isMoreThanMaxNumberOfPlans(long count) {
		return count >= MAX_NUMBER_OF_PLANS;
	}
}
