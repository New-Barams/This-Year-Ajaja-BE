package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.tag.application.DeletePlanTagService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DeletePlanService {
	private final LoadPlanService getPlanService;
	private final DeletePlanTagService deletePlanTagService;

	public void delete(Long id, Long userId, int month) {
		deletePlanTagService.delete(id);

		Plan plan = getPlanService.loadPlanOrElseThrow(id);
		plan.delete(userId, month);
	}
}
