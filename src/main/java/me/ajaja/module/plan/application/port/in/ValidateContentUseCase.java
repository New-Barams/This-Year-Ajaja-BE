package me.ajaja.module.plan.application.port.in;

import me.ajaja.module.plan.dto.BanWordValidationResult;
import me.ajaja.module.plan.dto.PlanRequest;

public interface ValidateContentUseCase {
	BanWordValidationResult check(PlanRequest.CheckBanWord request);
}
