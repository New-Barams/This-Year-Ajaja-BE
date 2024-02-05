package me.ajaja.module.plan.application;

import static me.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.plan.application.port.in.LoadPlanUseCase;
import me.ajaja.module.plan.application.port.out.FindPlanPort;
import me.ajaja.module.plan.domain.Plan;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class LoadPlanService implements LoadPlanUseCase {
	private final FindPlanPort findPlanPort;

	public Plan loadPlanOrElseThrow(Long id) {
		return findPlanPort.findById(id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_PLAN));
	}
}
