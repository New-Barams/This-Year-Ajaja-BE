package com.newbarams.ajaja.module.plan.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.plan.repository.PlanRepository;
import com.newbarams.ajaja.module.remind.application.CreateRemindService;
import com.newbarams.ajaja.module.tag.application.CreatePlanTagService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreatePlanService {
	private final PlanRepository planRepository;
	private final CreatePlanTagService createPlanTagService;
	private final CreateRemindService createRemindService;

	public PlanResponse.Create create(Long userId, PlanRequest.Create request, int month) {
		Plan plan = PlanMapper.toEntity(request, userId, month);
		Plan savedPlan = planRepository.save(plan);

		List<String> tags = createPlanTagService.create(savedPlan.getId(), request.tags());

		createRemindService.createRemind(plan.getUserId(), plan.getId(), plan.getMessages(), plan.getInfo());

		return PlanMapper.toResponse(savedPlan, tags);
	}
}
