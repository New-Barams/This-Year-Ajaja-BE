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
import com.newbarams.ajaja.module.plan.application.GetPlanService;
import com.newbarams.ajaja.module.plan.application.UpdatePlanService;
import com.newbarams.ajaja.module.plan.domain.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.domain.dto.PlanResponse;

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

	@Operation(description = "특정 목표 달성률 조회 API")
	@GetMapping("/feedbacks/{planId}")
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
	public void updatePlanPublicStatus(@PathVariable Long id) {
		updatePlanService.updatePublicStatus(id);
	}

	@Operation(summary = "계획 리마인드 알림 여부 변경 API")
	@PutMapping("/{id}/remindable")
	@ResponseStatus(OK)
	public void updatePlanRemindableStatus(@PathVariable Long id) {
		updatePlanService.updateRemindableStatus(id);
	}

	@Operation(summary = "계획 수정 API")
	@PutMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.Create> updatePlan(@PathVariable Long id,
		@RequestBody PlanRequest.Update request, @RequestHeader(name = "Date") String date) {
		PlanResponse.Create updated = updatePlanService.update(id, request, date);

		return new AjajaResponse<>(true, updated);
	}
}
