package com.newbarams.ajaja.module.remind.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.exception.ErrorResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.remind.application.port.in.UpdateRemindInfoUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "plan")
@RestController
@RequiredArgsConstructor
public class UpdateRemindInfoController {
	private final UpdateRemindInfoUseCase updateRemindInfoUseCase;

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
	@PutMapping("/plans/{planId}/reminds")
	@ResponseStatus(HttpStatus.OK)
	public AjajaResponse<Void> modifyRemindInfo(
		@UserId Long userId,
		@PathVariable Long planId,
		@RequestBody PlanRequest.UpdateRemind request
	) {
		updateRemindInfoUseCase.update(userId, planId, request);
		return AjajaResponse.ok();
	}
}
