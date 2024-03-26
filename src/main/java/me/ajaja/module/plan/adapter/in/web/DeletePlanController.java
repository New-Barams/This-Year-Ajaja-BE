package me.ajaja.module.plan.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.annotation.Login;
import me.ajaja.module.plan.application.port.in.DeletePlanUseCase;

@RestController
@RequiredArgsConstructor
class DeletePlanController {
	private final DeletePlanUseCase deletePlanUseCase;

	@Authorize
	@DeleteMapping("/plans/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<Void> deletePlan(
		@Login Long userId,
		@PathVariable Long id,
		@RequestHeader(name = "Month") @Min(1) @Max(12) int month
	) {
		deletePlanUseCase.delete(id, userId, month);
		return AjajaResponse.ok();
	}
}
