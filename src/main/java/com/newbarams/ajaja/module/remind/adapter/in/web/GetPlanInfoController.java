package com.newbarams.ajaja.module.remind.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.exception.ErrorResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.remind.application.port.in.GetPlanInfoUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "plan")
@RestController
@RequiredArgsConstructor
public class GetPlanInfoController {
	private final GetPlanInfoUseCase getPlanInfoUseCase;

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
	@GetMapping("/plans/main")
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanResponse.MainInfo>> getPlanInfo(@UserId Long userId) {
		List<PlanResponse.MainInfo> response = getPlanInfoUseCase.load(userId);
		return AjajaResponse.ok(response);
	}
}
