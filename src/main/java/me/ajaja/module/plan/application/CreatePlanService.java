package me.ajaja.module.plan.application;

import static me.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.PlanRepository;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.infra.PlanQueryRepository;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.tag.application.CreatePlanTagService;

@Service
@Transactional
@RequiredArgsConstructor
public class CreatePlanService {
	private static final int MAX_NUMBER_OF_PLANS = 4;

	private final PlanRepository planRepository;
	private final PlanQueryRepository planQueryRepository;
	private final CreatePlanTagService createPlanTagService;
	private final PlanMapper planMapper;

	public Long create(Long userId, PlanRequest.Create request, int month) {
		checkNumberOfUserPlans(userId);

		Plan plan = Plan.create(planMapper.toParam(userId, request, month));
		Plan savedPlan = planRepository.save(plan);

		createPlanTagService.create(savedPlan.getId(), request.getTags());

		return savedPlan.getId();
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
