package me.ajaja.module.plan.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.plan.application.port.in.CreatePlanUseCase;
import me.ajaja.module.plan.dto.PlanRequest;

@RestController
@RequiredArgsConstructor
class CreatePlanController {
	private final CreatePlanUseCase createPlanUseCase;

	@Authorize
	@PostMapping("/plans")
	@ResponseStatus(CREATED)
	public ResponseEntity<AjajaResponse<Void>> createPlan(
		@RequestBody PlanRequest.Create request,
		@RequestHeader(name = "Month") @Min(1) @Max(12) int month
	) {
		Long userId = SecurityUtil.getUserId();
		Long planId = createPlanUseCase.create(userId, request, month);
		URI uri = URI.create("plans/" + planId);

		return ResponseEntity.created(uri).body(AjajaResponse.ok());
	}
}
