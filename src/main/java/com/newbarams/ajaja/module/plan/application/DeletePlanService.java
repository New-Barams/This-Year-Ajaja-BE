package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DeletePlanService {
	private final GetPlanService getPlanService;

	public void delete(Long id, String date) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);
		plan.delete(date);
	}
}
