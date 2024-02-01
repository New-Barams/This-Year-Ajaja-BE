package me.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.PlanRepository;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.tag.application.UpdatePlanTagService;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdatePlanService {
	private final LoadPlanService getPlanService;
	private final UpdatePlanTagService updatePlanTagService;
	private final PlanRepository planRepository;
	private final PlanMapper planMapper;

	public void updatePublicStatus(Long id, Long userId) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updatePublicStatus(userId);
		planRepository.save(plan);
	}

	public void updateRemindStatus(Long id, Long userId) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updateRemindStatus(userId);
		planRepository.save(plan);
	}

	public void updateAjajaStatus(Long id, Long userId) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);

		plan.updateAjajaStatus(userId);
		planRepository.save(plan);
	}

	public PlanResponse.Detail update(Long id, Long userId, PlanRequest.Update request, int month) {
		Plan plan = getPlanService.loadPlanOrElseThrow(id);
		updatePlanTagService.update(id, request.getTags());

		plan.update(planMapper.toParam(userId, request, month));

		planRepository.save(plan);

		return getPlanService.loadByIdAndOptionalUser(userId, id);
	}
}
