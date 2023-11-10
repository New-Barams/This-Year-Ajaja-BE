package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdatePlanService {
	private final GetPlanService getPlanService;

	public void updatePublicStatus(Long id) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updatePublicStatus();
	}

	public void updateRemindableStatus(Long id) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updateRemindableStatus();
	}
}
