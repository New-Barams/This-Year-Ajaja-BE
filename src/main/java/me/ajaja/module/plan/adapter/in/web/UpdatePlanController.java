package me.ajaja.module.plan.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.plan.application.port.in.UpdatePlanUseCase;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;

@RestController
@RequiredArgsConstructor
class UpdatePlanController {
	private final UpdatePlanUseCase updatePlanUseCase;

	@Authorization
	@PutMapping("/plans/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.Detail> updatePlan(
		@PathVariable Long id,
		@RequestBody PlanRequest.Update request,
		@RequestHeader(name = "Month") @Min(1) @Max(12) int month
	) {
		Long userId = SecurityUtil.getUserId();
		PlanResponse.Detail response = updatePlanUseCase.update(id, userId, request, month);

		return AjajaResponse.ok(response);
	}
}
