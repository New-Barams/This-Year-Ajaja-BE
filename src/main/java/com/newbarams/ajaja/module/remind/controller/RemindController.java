package com.newbarams.ajaja.module.remind.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.module.remind.application.GetRemindInfoService;
import com.newbarams.ajaja.module.remind.application.LoadRemindInfoService;
import com.newbarams.ajaja.module.remind.domain.dto.GetRemindInfo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "remind")
@RestController
@RequiredArgsConstructor
@RequestMapping("/reminds")
public class RemindController {
	private final GetRemindInfoService getRemindInfoService;
	private final LoadRemindInfoService loadRemindInfoService;

	@Operation(summary = "비시즌일때 리마인드 조회 API")
	@GetMapping("/{planId}")
	@ResponseStatus(HttpStatus.OK)
	public AjajaResponse<GetRemindInfo> getRemindResponse(
		@PathVariable Long planId
	) {
		return new AjajaResponse<>(true, getRemindInfoService.getRemindInfo(planId));
	}

	@Operation(summary = "시즌일 때 리마인드 조회 API")
	@GetMapping("/modify/{planId}")
	@ResponseStatus(HttpStatus.OK)
	public AjajaResponse<GetRemindInfo> getRemindInfoResponse(
		@PathVariable Long planId
	) {
		return new AjajaResponse<>(true, loadRemindInfoService.loadRemindInfo(planId));
	}

}
