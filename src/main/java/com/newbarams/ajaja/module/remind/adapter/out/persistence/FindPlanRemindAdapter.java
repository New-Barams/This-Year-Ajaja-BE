package com.newbarams.ajaja.module.remind.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.remind.application.port.out.FindPlanRemindQuery;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;
import com.newbarams.ajaja.module.remind.mapper.RemindInfoMapper;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindPlanRemindAdapter implements FindPlanRemindQuery {
	private final LoadPlanService loadPlanService;
	private final RemindInfoMapper mapper;

	@Override
	public RemindResponse.RemindInfo findByUserIdAndPlanId(Long userId, Long planId) {
		Plan plan = loadPlanService.loadByUserIdAndPlanId(userId, planId);

		List<RemindResponse.Message> messages = plan.getMessages()
			.stream().map(mapper::toMessage)
			.toList();

		return mapper.toRemindInfo(plan, messages);
	}
}
