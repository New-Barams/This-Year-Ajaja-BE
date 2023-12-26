package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.tag.application.UpdatePlanTagService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdatePlanService {
	private final LoadPlanService getPlanService;
	private final UpdatePlanTagService updatePlanTagService;
	private final PlanRepository planRepository;
	private final PlanMapper planMapper;

	public void updatePublicStatus(Long id, Long userId) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updatePublicStatus(userId);
	}

	public void updateRemindStatus(Long id, Long userId) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updateRemindStatus(userId);
	}

	public void updateAjajaStatus(Long id, Long userId) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updateAjajaStatus(userId);
	}

	public PlanResponse.Detail update(Long id, Long userId, PlanRequest.Update request, int month) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);
		updatePlanTagService.update(id, request.tags());

		plan.update(planMapper.toParam(userId, request, month));

		planRepository.save(plan);

		return getPlanService.loadByIdAndOptionalUser(id, userId);
	}
}
