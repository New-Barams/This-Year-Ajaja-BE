package com.newbarams.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.global.exception.AjajaException;
import com.newbarams.ajaja.global.exception.ErrorCode;
import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.domain.PlanRepository;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.mapper.MessageMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateRemindInfoService {
	private final PlanRepository planRepository;
	private final MessageMapper mapper;

	public void updateRemindInfo(Long planId, PlanRequest.UpdateRemind request) {
		Plan plan = planRepository.findById(planId)
			.orElseThrow(() -> AjajaException.withId(planId, ErrorCode.NOT_FOUND_PLAN));

		plan.updateRemind(mapper.toDomain(request), mapper.toDomain(request.getMessages()));
	}
}
