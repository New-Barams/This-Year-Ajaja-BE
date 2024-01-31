package com.newbarams.ajaja.module.plan.presentation;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.global.security.jwt.util.JwtParser;
import com.newbarams.ajaja.global.util.BearerUtils;
import com.newbarams.ajaja.module.ajaja.application.SwitchAjajaService;
import com.newbarams.ajaja.module.plan.application.CreatePlanService;
import com.newbarams.ajaja.module.plan.application.DeletePlanService;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.application.UpdatePlanService;
import com.newbarams.ajaja.module.plan.application.ValidateContentService;
import com.newbarams.ajaja.module.plan.dto.BanWordValidationResult;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/plans")
public class PlanController {
	private final CreatePlanService createPlanService;
	private final LoadPlanService getPlanService;
	private final DeletePlanService deletePlanService;
	private final UpdatePlanService updatePlanService;
	private final SwitchAjajaService switchAjajaService;
	private final ValidateContentService validateContentService;

	private final JwtParser jwtParser; // todo: delete when authentication filtering update

	@PostMapping("/{id}/ajaja")
	public AjajaResponse<Void> switchAjaja(@UserId Long userId, @PathVariable Long id) {
		switchAjajaService.switchOrAddIfNotExist(userId, id);
		return AjajaResponse.ok();
	}

	@GetMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.Detail> getPlanWithOptionalUser(
		@RequestHeader(value = AUTHORIZATION, required = false) String accessToken,
		@PathVariable Long id
	) {
		Long userId = accessToken == null ? null : parseUserId(accessToken);
		PlanResponse.Detail response = getPlanService.loadByIdAndOptionalUser(userId, id);
		return AjajaResponse.ok(response);
	}

	private Long parseUserId(String accessToken) {
		BearerUtils.validate(accessToken);
		return jwtParser.parseId(BearerUtils.resolve(accessToken));
	}

	@PostMapping
	@ResponseStatus(CREATED)
	public ResponseEntity<AjajaResponse<Void>> createPlan(
		@UserId Long userId,
		@RequestBody PlanRequest.Create request,
		@RequestHeader(name = "Month") @Min(1) @Max(12) int month
	) {
		Long planId = createPlanService.create(userId, request, month);
		URI uri = URI.create("plans/" + planId);

		return ResponseEntity.created(uri).body(AjajaResponse.ok());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<Void> deletePlan(
		@PathVariable Long id,
		@UserId Long userId,
		@RequestHeader(name = "Month") @Min(1) @Max(12) int month
	) {
		deletePlanService.delete(id, userId, month);
		return AjajaResponse.ok();
	}

	@PutMapping("/{id}/public")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updatePlanPublicStatus(@PathVariable Long id, @UserId Long userId) {
		updatePlanService.updatePublicStatus(id, userId);
		return AjajaResponse.ok();
	}

	@PutMapping("/{id}/remindable")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updatePlanRemindStatus(@PathVariable Long id, @UserId Long userId) {
		updatePlanService.updateRemindStatus(id, userId);
		return AjajaResponse.ok();
	}

	@PutMapping("/{id}/ajaja")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updatePlanAjajaStatus(@PathVariable Long id, @UserId Long userId) {
		updatePlanService.updateAjajaStatus(id, userId);
		return AjajaResponse.ok();
	}

	@PutMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.Detail> updatePlan(
		@PathVariable Long id,
		@UserId Long userId,
		@RequestBody PlanRequest.Update request,
		@RequestHeader(name = "Month") @Min(1) @Max(12) int month
	) {
		PlanResponse.Detail response = updatePlanService.update(id, userId, request, month);
		return AjajaResponse.ok(response);
	}

	@GetMapping
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanResponse.GetAll>> getAllPlans(@ModelAttribute PlanRequest.GetAll request) {
		List<PlanResponse.GetAll> responses = getPlanService.loadAllPlans(request);
		return AjajaResponse.ok(responses);
	}

	@PostMapping("/validate")
	@ResponseStatus(OK)
	public AjajaResponse<Map<String, BanWordValidationResult>> validateBanWord(
		@RequestBody PlanRequest.CheckBanWord request
	) {
		Map<String, BanWordValidationResult> response = validateContentService.check(request);
		return AjajaResponse.ok(response);
	}
}
