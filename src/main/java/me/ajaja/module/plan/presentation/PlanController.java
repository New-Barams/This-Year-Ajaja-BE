package me.ajaja.module.plan.presentation;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.*;

import java.net.URI;
import java.util.List;

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

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.global.security.jwt.JwtParser;
import me.ajaja.global.util.BearerUtil;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.ajaja.application.SwitchAjajaService;
import me.ajaja.module.plan.application.CreatePlanService;
import me.ajaja.module.plan.application.DeletePlanService;
import me.ajaja.module.plan.application.LoadPlanService;
import me.ajaja.module.plan.application.UpdatePlanService;
import me.ajaja.module.plan.application.ValidateContentService;
import me.ajaja.module.plan.dto.BanWordValidationResult;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.plan.dto.PlanResponse;

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

	@Authorization
	@PostMapping("/{id}/ajaja")
	@ResponseStatus(OK)
	public AjajaResponse<Void> switchAjaja(@PathVariable Long id) {
		Long userId = SecurityUtil.getUserId();
		switchAjajaService.switchOrAddIfNotExist(userId, id);
		return AjajaResponse.ok();
	}

	@GetMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.Detail> getPlanWithOptionalUser(
		@RequestHeader(value = AUTHORIZATION, required = false) String accessToken,
		@PathVariable Long id
	) {
		Long userId = parseUserId(accessToken);
		PlanResponse.Detail response = getPlanService.loadByIdAndOptionalUser(userId, id);
		return AjajaResponse.ok(response);
	}

	private Long parseUserId(String accessToken) {
		if (accessToken == null) {
			return null;
		}

		BearerUtil.validate(accessToken);
		return jwtParser.parseId(BearerUtil.resolve(accessToken));
	}

	@Authorization
	@PostMapping
	@ResponseStatus(CREATED)
	public ResponseEntity<AjajaResponse<Void>> createPlan(
		@RequestBody PlanRequest.Create request,
		@RequestHeader(name = "Month") @Min(1) @Max(12) int month
	) {
		Long userId = SecurityUtil.getUserId();
		Long planId = createPlanService.create(userId, request, month);
		URI uri = URI.create("plans/" + planId);

		return ResponseEntity.created(uri).body(AjajaResponse.ok());
	}

	@Authorization
	@DeleteMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<Void> deletePlan(
		@PathVariable Long id,
		@RequestHeader(name = "Month") @Min(1) @Max(12) int month
	) {
		Long userId = SecurityUtil.getUserId();
		deletePlanService.delete(id, userId, month);
		return AjajaResponse.ok();
	}

	@Authorization
	@PutMapping("/{id}/public")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updatePlanPublicStatus(@PathVariable Long id) {
		Long userId = SecurityUtil.getUserId();
		updatePlanService.updatePublicStatus(id, userId);
		return AjajaResponse.ok();
	}

	@Authorization
	@PutMapping("/{id}/remindable")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updatePlanRemindStatus(@PathVariable Long id) {
		Long userId = SecurityUtil.getUserId();
		updatePlanService.updateRemindStatus(id, userId);
		return AjajaResponse.ok();
	}

	@Authorization
	@PutMapping("/{id}/ajaja")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updatePlanAjajaStatus(@PathVariable Long id) {
		Long userId = SecurityUtil.getUserId();
		updatePlanService.updateAjajaStatus(id, userId);
		return AjajaResponse.ok();
	}

	@Authorization
	@PutMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.Detail> updatePlan(
		@PathVariable Long id,
		@RequestBody PlanRequest.Update request,
		@RequestHeader(name = "Month") @Min(1) @Max(12) int month
	) {
		Long userId = SecurityUtil.getUserId();
		PlanResponse.Detail response = updatePlanService.update(id, userId, request, month);
		return AjajaResponse.ok(response);
	}

	@GetMapping
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanResponse.GetAll>> getAllPlans(@ModelAttribute PlanRequest.GetAll request) {
		List<PlanResponse.GetAll> responses = getPlanService.loadAllPlans(request);
		return AjajaResponse.ok(responses);
	}

	@Authorization
	@PostMapping("/validate")
	@ResponseStatus(OK)
	public AjajaResponse<BanWordValidationResult> validateBanWord(
		@Valid @RequestBody PlanRequest.CheckBanWord request
	) {
		BanWordValidationResult response = validateContentService.check(request);
		return AjajaResponse.ok(response);
	}
}
