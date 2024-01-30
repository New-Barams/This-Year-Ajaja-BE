package com.newbarams.ajaja.module.remind.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.annotation.UserId;
import com.newbarams.ajaja.module.plan.dto.PlanResponse;
import com.newbarams.ajaja.module.remind.application.port.in.GetPlanInfoUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GetPlanInfoController {
	private final GetPlanInfoUseCase getPlanInfoUseCase;

	@GetMapping("/plans/main")
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanResponse.MainInfo>> getPlanInfo(@UserId Long userId) {
		List<PlanResponse.MainInfo> response = getPlanInfoUseCase.load(userId);
		return AjajaResponse.ok(response);
	}
}
