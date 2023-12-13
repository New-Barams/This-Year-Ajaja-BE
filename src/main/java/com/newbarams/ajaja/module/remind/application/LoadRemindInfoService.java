package com.newbarams.ajaja.module.remind.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Message;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;
import com.newbarams.ajaja.module.remind.mapper.RemindInfoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoadRemindInfoService {
	private final LoadPlanService loadPlanService;
	private final RemindInfoMapper remindInfoMapper;

	public RemindResponse.CommonResponse loadRemindInfo(Long planId) {
		Plan plan = loadPlanService.loadPlanOrElseThrow(planId);
		List<RemindResponse.Response> responses = new ArrayList<>();
		int remindMonth = plan.getRemindMonth();
		int remindTerm = plan.getRemindTerm();

		for (Message message : plan.getMessages()) {
			responses.add(
				remindInfoMapper.toFutureMessages(remindMonth, plan.getRemindDate(), message.getContent()));
			remindMonth += remindTerm;
		}

		return new RemindResponse.CommonResponse(
			plan.getRemindTimeName(),
			plan.getRemindDate(),
			plan.getRemindTerm(),
			plan.getRemindTotalPeriod(),
			plan.getIsRemindable(),
			responses
		);
	}
}
