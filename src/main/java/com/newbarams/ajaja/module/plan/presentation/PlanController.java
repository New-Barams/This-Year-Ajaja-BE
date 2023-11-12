package com.newbarams.ajaja.module.plan.presentation;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.module.plan.application.CreatePlanService;
import com.newbarams.ajaja.module.plan.application.DeletePlanService;
import com.newbarams.ajaja.module.plan.application.GetPlanAchieveService;
import com.newbarams.ajaja.module.plan.application.GetPlanInfoService;
import com.newbarams.ajaja.module.plan.application.GetPlanService;
import com.newbarams.ajaja.module.plan.application.UpdatePlanService;
import com.newbarams.ajaja.module.plan.dto.PlanInfo;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "plan")
@RestController
@RequiredArgsConstructor
@RequestMapping("/plans")
public class PlanController {
	private final CreatePlanService createPlanService;
	private final GetPlanService getPlanService;
	private final DeletePlanService deletePlanService;
	private final GetPlanAchieveService getPlanAchieveService;
	private final UpdatePlanService updatePlanService;
	private final GetPlanInfoService getPlanInfoService;

	@Operation(summary = "계획 생성 API")
	@PostMapping
	@ResponseStatus(CREATED)
	public AjajaResponse<PlanResponse.Create> createPlan(@RequestBody PlanRequest.Create request) {
		PlanResponse.Create response = createPlanService.create(request);

		return new AjajaResponse<>(true, response);
	}

	@Operation(summary = "계획 단건 조회 API")
	@GetMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.GetOne> getPlan(@PathVariable Long id) {
		PlanResponse.GetOne response = getPlanService.loadById(id);

		return new AjajaResponse<>(true, response);
	}

	@Operation(summary = "계획 삭제 API")
	@DeleteMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse deletePlan(@PathVariable Long id, @RequestHeader(name = "Date") String date) {
		deletePlanService.delete(id, date);

		return new AjajaResponse<>(true, null);
	}

	@Operation(summary = "특정 목표 달성률 조회 API")
	@GetMapping("/{planId}/feedbacks")
	@ResponseStatus(OK)
	public AjajaResponse<Integer> getPlanAchieve(
		@PathVariable Long planId
	) {
		int totalAchieve = getPlanAchieveService.calculatePlanAchieve(planId);

		return new AjajaResponse<>(true, totalAchieve);
	}

	@Operation(summary = "계획 공개 여부 변경 API")
	@PutMapping("/{id}/public")
	@ResponseStatus(OK)
	public AjajaResponse updatePlanPublicStatus(@PathVariable Long id) {
		updatePlanService.updatePublicStatus(id);

		return new AjajaResponse(true, null);
	}

	@Operation(summary = "계획 리마인드 알림 여부 변경 API")
	@PutMapping("/{id}/remindable")
	@ResponseStatus(OK)
	public AjajaResponse updatePlanRemindStatus(@PathVariable Long id) {
		updatePlanService.updateRemindStatus(id);

		return new AjajaResponse(true, null);
	}

	@Operation(summary = "응원메시지 알림 여부 변경 API")
	@PutMapping("/{id}/ajaja")
	@ResponseStatus(OK)
	public AjajaResponse updatePlanAjajaStatus(@PathVariable Long id) {
		updatePlanService.updateAjajaStatus(id);

		return new AjajaResponse(true, null);
	}

	@Operation(summary = "계획 수정 API")
	@PutMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.Create> updatePlan(@PathVariable Long id,
		@RequestBody PlanRequest.Update request, @RequestHeader(name = "Date") String date) {
		PlanResponse.Create updated = updatePlanService.update(id, request, date);

		return new AjajaResponse<>(true, updated);

	}

	@Operation(description = "메인페이지 목표 조회 API")
	@GetMapping("/main/{userId}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanInfo.PlanInfoResponse> getPlanInfo(
		@PathVariable Long userId
	) {
		PlanInfo.PlanInfoResponse planInfoResponse = getPlanInfoService.loadPlanInfo(userId);

		return new AjajaResponse<>(true, planInfoResponse);
	}
}
