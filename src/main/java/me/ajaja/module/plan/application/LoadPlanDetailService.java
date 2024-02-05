package me.ajaja.module.plan.application;

import static me.ajaja.global.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.exception.AjajaException;
import me.ajaja.module.feedback.application.model.PlanFeedbackInfo;
import me.ajaja.module.plan.application.port.in.LoadPlanDetailUseCase;
import me.ajaja.module.plan.application.port.out.FindPlanDetailPort;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.plan.mapper.PlanMapper;
import me.ajaja.module.remind.application.port.out.FindTargetRemindQuery;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class LoadPlanDetailService implements LoadPlanDetailUseCase {
	private final FindPlanDetailPort findPlanDetailPort;
	private final FindTargetRemindQuery findTargetRemindQuery;
	private final PlanMapper planMapper;

	@Override
	public PlanResponse.Detail loadByIdAndOptionalUser(Long userId, Long id) {
		return findPlanDetailPort.findPlanDetailByIdAndOptionalUser(userId, id)
			.orElseThrow(() -> AjajaException.withId(id, NOT_FOUND_PLAN));
	}

	@Override
	public Plan loadByUserIdAndPlanId(Long userId, Long id) {
		return findTargetRemindQuery.loadByUserIdAndPlanId(userId, id);
	}

	@Override
	public PlanFeedbackInfo loadPlanFeedbackInfoByPlanId(Long userId, Long planId) {
		Plan plan = loadByUserIdAndPlanId(userId, planId);
		return planMapper.toModel(plan);
	}
}
