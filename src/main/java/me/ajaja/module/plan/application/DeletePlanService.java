package me.ajaja.module.plan.application;

import static me.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.plan.application.port.in.DeletePlanUseCase;
import me.ajaja.module.plan.application.port.out.FindPlanPort;
import me.ajaja.module.plan.application.port.out.SavePlanPort;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.tag.application.DeletePlanTagService;

@Service
@Transactional
@RequiredArgsConstructor
class DeletePlanService implements DeletePlanUseCase {
	private final FindPlanPort findPlanPort;
	private final DeletePlanTagService deletePlanTagService;
	private final SavePlanPort savePlanPort;

	@Override
	public void delete(Long id, Long userId, int month) {
		deletePlanTagService.delete(id);

		Plan plan = findPlanPort.findById(id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_PLAN));

		plan.delete(userId, month);

		savePlanPort.save(plan);
	}
}
