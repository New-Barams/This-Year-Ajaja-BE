package me.ajaja.module.plan.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.module.plan.application.ValidateContentService;
import me.ajaja.module.plan.dto.BanWordValidationResult;
import me.ajaja.module.plan.dto.PlanRequest;

@Validated
@RestController
@RequiredArgsConstructor
public class ValidateBanWordController {
	private final ValidateContentService validateContentService;

	@Authorization
	@PostMapping("/plans/validate")
	@ResponseStatus(OK)
	public AjajaResponse<BanWordValidationResult> validateBanWord(
		@Valid @RequestBody PlanRequest.CheckBanWord request
	) {
		BanWordValidationResult response = validateContentService.check(request);
		return AjajaResponse.ok(response);
	}
}
