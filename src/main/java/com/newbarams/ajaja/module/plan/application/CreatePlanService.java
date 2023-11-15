package com.newbarams.ajaja.module.plan.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.newbarams.ajaja.module.plan.domain.Plan;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.plan.mapper.PlanMapper;
import com.newbarams.ajaja.module.plan.repository.PlanRepository;
import com.newbarams.ajaja.module.tag.application.CreatePlanTagService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CreatePlanService {
	private final PlanRepository planRepository;
	private final CreatePlanTagService createPlanTagService;

	public PlanResponse.Create create(PlanRequest.Create request) {
		Plan plan = PlanMapper.toEntity(request, 1L);
		Plan savedPlan = planRepository.save(plan);

		List<String> tags = createPlanTagService.create(savedPlan.getId(), request.tags());

		return PlanMapper.toResponse(savedPlan, tags);
	}
}
