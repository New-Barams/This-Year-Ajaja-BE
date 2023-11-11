package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.tag.application.TagService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdatePlanService {
	private final GetPlanService getPlanService;
	private final TagService tagService;

	public void updatePublicStatus(Long id) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updatePublicStatus();
	}

	public void updateRemindStatus(Long id) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updateRemindStatus();
	}

	public void updateAjajaStatus(Long id) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updateAjajaStatus();
	}

	public PlanResponse.Create update(Long id, PlanRequest.Update request, String date) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.update(
			date,
			request.title(),
			request.description(),
			request.remindTotalPeriod(),
			request.remindTerm(),
			request.remindDate(),
			request.remindTime(),
			request.isPublic(),
			request.canRemind(),
			request.canAjaja(),
			tagService.getTags(request.tags()),
			PlanMapper.convertToMessageList(request.messages())
		);

		return PlanMapper.toResponse(plan);
	}
}
