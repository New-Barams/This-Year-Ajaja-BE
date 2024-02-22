package me.ajaja.module.plan.application;

import static me.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.plan.application.port.in.SwitchPlanStatusUseCase;
import me.ajaja.module.plan.application.port.out.FindPlanPort;
import me.ajaja.module.plan.application.port.out.SavePlanPort;
import me.ajaja.module.plan.domain.Plan;

@Service
@Transactional
@RequiredArgsConstructor
class SwitchPlanStatusService implements SwitchPlanStatusUseCase {
	private final SavePlanPort savePlanPort;
	private final FindPlanPort findPlanPort;

	@Override
	public void switchPublic(Long id, Long userId) {
		Plan plan = loadPlanOrElseThrow(id);

		plan.updatePublicStatus(userId);
		savePlanPort.save(plan);
	}

	@Override
	public void switchRemindable(Long id, Long userId) {
		Plan plan = loadPlanOrElseThrow(id);

		plan.updateRemindStatus(userId);
		savePlanPort.save(plan);
	}

	@Override
	public void switchAjajable(Long id, Long userId) {
		Plan plan = loadPlanOrElseThrow(id);

		plan.updateAjajaStatus(userId);
		savePlanPort.save(plan);
	}

	private Plan loadPlanOrElseThrow(Long id) {
		return findPlanPort.findById(id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_PLAN));
	}
}
