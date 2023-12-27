package com.newbarams.ajaja.module.remind.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.RemindQueryRepository;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoadRemindInfoService {
	private final LoadPlanService loadPlanService;
	private final RemindQueryRepository remindQueryRepository;

	public RemindResponse.RemindInfo loadRemindInfoResponse(Long planId) {
		Plan plan = loadPlanService.loadPlanOrElseThrow(planId);
		return remindQueryRepository.findAllReminds(plan);
	}
}
