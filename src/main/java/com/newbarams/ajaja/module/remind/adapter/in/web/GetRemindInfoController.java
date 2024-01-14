package com.newbarams.ajaja.module.remind.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.exception.ErrorResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.remind.application.port.in.GetRemindInfoUseCase;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "remind")
@RestController
@RequiredArgsConstructor
public class GetRemindInfoController {
	private final GetRemindInfoUseCase getRemindInfoUseCase;

	@Operation(summary = "[토큰 필요] 리마인드 정보 조회 API", description = "<b>url에 플랜id 값이 필요합니다.</b>",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공적으로 계획에 대한 정보를 불러왔습니다."),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "계획 정보를 불러오지 못했습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 문제입니다. 관리자에게 문의 바랍니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		})
	@GetMapping("/reminds/{planId}")
	@ResponseStatus(HttpStatus.OK)
	public AjajaResponse<RemindResponse.RemindInfo> getRemindResponse(
		@UserId Long userId,
		@PathVariable Long planId
	) {
		return AjajaResponse.ok(getRemindInfoUseCase.load(userId, planId));
	}
}
