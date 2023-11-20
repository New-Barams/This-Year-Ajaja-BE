package com.newbarams.ajaja.module.remind.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.common.error.ErrorResponse;
import com.newbarams.ajaja.module.remind.application.GetRemindInfoService;
import com.newbarams.ajaja.module.remind.application.LoadRemindInfoService;
import com.newbarams.ajaja.module.remind.domain.dto.GetRemindInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "remind")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reminds")
public class RemindController {
	private final GetRemindInfoService getRemindInfoService;
	private final LoadRemindInfoService loadRemindInfoService;

	@Operation(summary = "[토큰 필요] 비시즌일때 리마인드 조회 API", description = "<b>url에 플랜id 값이 필요합니다.</b>",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공적으로 계획에 대한 정보를 불러왔습니다."),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "404", description = "계획 정보를 불러오지 못했습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
			@ApiResponse(responseCode = "500", description = "서버 내부 문제입니다. 관리자에게 문의 바랍니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
		})
	@GetMapping("/{planId}")
	@ResponseStatus(HttpStatus.OK)
	public AjajaResponse<GetRemindInfo> getRemindResponse(
		@PathVariable Long planId
	) {
		return new AjajaResponse<>(true, getRemindInfoService.getRemindInfo(planId));
	}

	@Operation(summary = "[토큰 필요] 시즌일 때 리마인드 조회 API", description = "<b>url에 플랜id 값이 필요합니다.</b>",
		responses = {
			@ApiResponse(responseCode = "200", description = "성공적으로 계획에 대한 정보를 불러왔습니다."),
			@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다."),
			@ApiResponse(responseCode = "400", description = "계획 정보를 불러오지 못했습니다."),
			@ApiResponse(responseCode = "500", description = "서버 내부 문제입니다. 관리자에게 문의 바랍니다.")
		})
	@GetMapping("/modify/{planId}")
	@ResponseStatus(HttpStatus.OK)
	public AjajaResponse<GetRemindInfo> getRemindInfoResponse(
		@PathVariable Long planId
	) {
		return new AjajaResponse<>(true, loadRemindInfoService.loadRemindInfo(planId));
	}

}
