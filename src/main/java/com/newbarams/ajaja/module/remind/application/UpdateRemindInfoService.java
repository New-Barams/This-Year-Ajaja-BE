package com.newbarams.ajaja.module.remind.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.mapper.MessageMapper;
import com.newbarams.ajaja.module.remind.application.port.in.UpdateRemindInfoUseCase;
import com.newbarams.ajaja.module.remind.application.port.out.FindPlanPort;
import com.newbarams.ajaja.module.remind.application.port.out.SavePlanPort;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateRemindInfoService implements UpdateRemindInfoUseCase {
	private final FindPlanPort findPlanPort;
	private final SavePlanPort savePlanPort;
	private final MessageMapper mapper;

	public void update(Long userId, Long planId, PlanRequest.UpdateRemind request) {
		Plan plan = findPlanPort.findByUserIdAndPlanId(userId, planId);
		plan.updateRemind(mapper.toDomain(request), mapper.toDomain(request.getMessages()));
		savePlanPort.update(plan);
	}
}
