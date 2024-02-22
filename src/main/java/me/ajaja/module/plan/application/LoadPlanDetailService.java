package me.ajaja.module.plan.application;

import static me.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.plan.application.port.in.LoadPlanDetailUseCase;
import me.ajaja.module.plan.application.port.out.FindPlanDetailPort;
import me.ajaja.module.plan.dto.PlanResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class LoadPlanDetailService implements LoadPlanDetailUseCase {
	private final FindPlanDetailPort findPlanDetailPort;

	@Override
	public PlanResponse.Detail loadByIdAndOptionalUser(Long userId, Long id) {
		return findPlanDetailPort.findPlanDetailByIdAndOptionalUser(userId, id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_PLAN));
	}
}
