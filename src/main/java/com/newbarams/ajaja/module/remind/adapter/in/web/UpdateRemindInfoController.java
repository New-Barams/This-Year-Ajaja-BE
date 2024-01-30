package com.newbarams.ajaja.module.remind.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.annotation.UserId;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.remind.application.port.in.UpdateRemindInfoUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UpdateRemindInfoController {
	private final UpdateRemindInfoUseCase updateRemindInfoUseCase;

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
