package com.newbarams.ajaja.module.remind.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.domain.dto.GetRemindInfo;
import com.newbarams.ajaja.module.remind.mapper.RemindMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadRemindInfoService {
	private final LoadPlanService loadPlanService;
	private final RemindMapper remindMapper;

	public GetRemindInfo.CommonResponse loadRemindInfo(Long planId) {
		Plan plan = loadPlanService.loadPlanOrElseThrow(planId);

		return remindMapper.toFutureRemind(plan);
	}
}
