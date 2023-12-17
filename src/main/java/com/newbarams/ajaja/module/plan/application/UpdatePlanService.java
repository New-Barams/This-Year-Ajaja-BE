package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.tag.application.UpdatePlanTagService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdatePlanService {
	private final LoadPlanService getPlanService;
	private final UpdatePlanTagService updatePlanTagService;

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

	public PlanResponse.GetOne update(Long id, Long userId, PlanRequest.Update request, int month) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);
		updatePlanTagService.update(id, request.tags());

		plan.update(
			userId,
			month,
			request.title(),
			request.description(),
			request.remindTotalPeriod(),
			request.remindTerm(),
			request.remindDate(),
			request.remindTime(),
			request.isPublic(),
			request.canRemind(),
			request.canAjaja()
		);

		return getPlanService.loadById(id, userId);
	}
}
