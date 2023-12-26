package com.newbarams.ajaja.module.plan.presentation;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.http.HttpStatus;
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
import com.newbarams.ajaja.global.exception.ErrorResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.global.security.jwt.util.JwtParser;
import com.newbarams.ajaja.module.ajaja.application.SwitchAjajaService;
import com.newbarams.ajaja.module.plan.application.CreatePlanService;
import com.newbarams.ajaja.module.plan.application.DeletePlanService;
import com.newbarams.ajaja.module.plan.application.LoadPlanInfoService;
import com.newbarams.ajaja.module.plan.application.LoadPlanService;
import com.newbarams.ajaja.module.plan.application.UpdatePlanService;
import com.newbarams.ajaja.module.plan.application.UpdateRemindInfoService;
import com.newbarams.ajaja.module.plan.dto.PlanInfoResponse;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Tag(name = "plan")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/plans")
public class PlanController {
	private final CreatePlanService createPlanService;
	private final LoadPlanService getPlanService;
	private final DeletePlanService deletePlanService;
	private final UpdatePlanService updatePlanService;
	private final LoadPlanInfoService loadPlanInfoService;
	private final UpdateRemindInfoService updateRemindInfoService;
	private final SwitchAjajaService switchAjajaService;

	private final JwtParser jwtParser; // todo: delete when authentication filtering update

	@PostMapping("/{id}/ajaja")
	public AjajaResponse<Void> switchAjaja(@UserId Long userId, @PathVariable Long id) {
		switchAjajaService.switchOrAddIfNotExist(userId, id);
		return AjajaResponse.ok();
	}

	@Operation(summary = "계획 단건 조회 API", description = "토큰을 추가해서 보내면 계획 작성자인지, 아좌좌를 눌렀는지 추가적으로 판별합니다.")
	@GetMapping("/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.Detail> getPlanWithOptionalUser(
		@RequestHeader(value = AUTHORIZATION, required = false) String accessToken,
		@PathVariable Long id
	) {
		Long userId = accessToken == null ? null : jwtParser.parseId(accessToken);
		PlanResponse.Detail response = getPlanService.loadByIdAndOptionalUser(userId, id);
		return AjajaResponse.ok(response);
	}

	@Operation(summary = "[토큰 필요] 계획 생성 API")
	@PostMapping
	@ResponseStatus(CREATED)
	public AjajaResponse<PlanResponse.Create> createPlan(
		@UserId Long userId,
		@RequestBody PlanRequest.Create request,
		@RequestHeader(name = "Month") @Min(1) @Max(12) int month
	) {
		PlanResponse.Create response = createPlanService.create(userId, request, month);
		return AjajaResponse.ok(response);
	}

	@Operation(summary = "[토큰 필요] 계획 삭제 API")
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

	@Operation(summary = "[토큰 필요] 계획 공개 여부 변경 API")
	@PutMapping("/{id}/public")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updatePlanPublicStatus(@PathVariable Long id, @UserId Long userId) {
		updatePlanService.updatePublicStatus(id, userId);
		return AjajaResponse.ok();
	}

	@Operation(summary = "[토큰 필요] 계획 리마인드 알림 여부 변경 API")
	@PutMapping("/{id}/remindable")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updatePlanRemindStatus(@PathVariable Long id, @UserId Long userId) {
		updatePlanService.updateRemindStatus(id, userId);
		return AjajaResponse.ok();
	}

	@Operation(summary = "[토큰 필요] 응원메시지 알림 여부 변경 API")
	@PutMapping("/{id}/ajaja")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updatePlanAjajaStatus(@PathVariable Long id, @UserId Long userId) {
		updatePlanService.updateAjajaStatus(id, userId);
		return AjajaResponse.ok();
	}

	@Operation(summary = "[토큰 필요] 계획 수정 API")
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

	@Operation(summary = "[토큰 필요] 메인 페이지 내 계획 조회 API", description = "로그인을 했을 시에만 불러올 수 있습니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공적으로 계획에 대한 정보를 불러왔습니다."),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 문제입니다. 관리자에게 문의 바랍니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		})
	@GetMapping("/main")
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanInfoResponse.GetPlanInfoResponse>> getPlanInfo(@UserId Long userId) {
		List<PlanInfoResponse.GetPlanInfoResponse> response = loadPlanInfoService.loadPlanInfo(userId);
		return AjajaResponse.ok(response);
	}

	@Operation(summary = "계획 전체 조회 API")
	@GetMapping
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanResponse.GetAll>> getAllPlans(@ModelAttribute PlanRequest.GetAll request) {
		List<PlanResponse.GetAll> responses = getPlanService.loadAllPlans(request);
		return AjajaResponse.ok(responses);
	}

	@Operation(summary = "[토큰 필요] 리마인드 정보 수정 API", description = "<b>url에 플랜id 값이 필요합니다.</b>",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공적으로 리마인드 정보를 수정하였습니다."),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "400", description = "변경 가능한 기간이 아닙니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "계획 정보를 불러오지 못했습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 문제입니다. 관리자에게 문의 바랍니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		})
	@PutMapping("/{planId}/reminds")
	@ResponseStatus(HttpStatus.OK)
	public AjajaResponse<Void> modifyRemindInfo(
		@PathVariable Long planId,
		@RequestBody PlanRequest.UpdateRemind request
	) {
		updateRemindInfoService.updateRemindInfo(planId, request);
		return AjajaResponse.ok();
	}
}
