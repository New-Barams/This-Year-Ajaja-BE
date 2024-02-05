package me.ajaja.module.plan.application.port.in;

import me.ajaja.module.feedback.application.model.PlanFeedbackInfo;
import me.ajaja.module.plan.domain.Plan;
import me.ajaja.module.plan.dto.PlanResponse;

public interface LoadPlanDetailUseCase {
	PlanResponse.Detail loadByIdAndOptionalUser(Long userId, Long id);

	Plan loadByUserIdAndPlanId(Long userId, Long id);

	PlanFeedbackInfo loadPlanFeedbackInfoByPlanId(Long userId, Long planId);
}
