package me.ajaja.module.plan.application;

import static me.ajaja.global.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.feedback.application.model.PlanFeedbackInfo;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.PlanRepository;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.plan.infra.PlanQueryRepository;
import me.ajaja.module.plan.mapper.PlanMapper;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadPlanService {
	private final PlanRepository planRepository;
	private final PlanQueryRepository planQueryRepository;
	private final PlanMapper planMapper;

	public PlanResponse.Detail loadByIdAndOptionalUser(Long userId, Long id) {
		return planQueryRepository.findPlanDetailByIdAndOptionalUser(userId, id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_PLAN));
	}

	public Plan loadByUserIdAndPlanId(Long userId, Long id) {
		return planQueryRepository.findByUserIdAndPlanId(userId, id);
	}

	public Plan loadPlanOrElseThrow(Long id) {
		return planRepository.findById(id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_PLAN));
	}

	public PlanFeedbackInfo loadPlanFeedbackInfoByPlanId(Long userId, Long planId) {
		Plan plan = loadByUserIdAndPlanId(userId, planId);
		return planMapper.toModel(plan);
	}

	public List<PlanResponse.GetAll> loadAllPlans(PlanRequest.GetAll request) {
		return planQueryRepository.findAllByCursorAndSorting(request);
	}
}
