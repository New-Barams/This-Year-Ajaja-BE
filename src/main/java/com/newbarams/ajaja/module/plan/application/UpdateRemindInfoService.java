package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.infra.PlanQueryRepository;
import com.newbarams.ajaja.module.plan.mapper.MessageMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateRemindInfoService {
	private final PlanQueryRepository planQueryRepository;
	private final PlanRepository planRepository;
	private final MessageMapper mapper;

	public void updateRemindInfo(Long userId, Long planId, PlanRequest.UpdateRemind request) {
		Plan plan = planQueryRepository.findByUserIdAndPlanId(userId, planId);

		plan.updateRemind(mapper.toDomain(request), mapper.toDomain(request.getMessages()));
		planRepository.save(plan);
	}
}
