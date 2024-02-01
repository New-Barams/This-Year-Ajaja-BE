package me.ajaja.module.plan.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.domain.PlanRepository;
import me.ajaja.module.tag.application.DeletePlanTagService;

@Service
@Transactional
@RequiredArgsConstructor
public class DeletePlanService {
	private final LoadPlanService getPlanService;
	private final DeletePlanTagService deletePlanTagService;
	private final PlanRepository planRepository;

	public void delete(Long id, Long userId, int month) {
		deletePlanTagService.delete(id);

		Plan plan = getPlanService.loadPlanOrElseThrow(id);
		plan.delete(userId, month);

		planRepository.save(plan);
	}
}
