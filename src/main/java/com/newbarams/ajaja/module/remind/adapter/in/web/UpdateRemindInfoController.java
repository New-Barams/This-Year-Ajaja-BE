package com.newbarams.ajaja.module.remind.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.annotation.Authorization;
import com.newbarams.ajaja.global.util.SecurityUtil;
import com.newbarams.ajaja.module.plan.dto.PlanRequest;
import com.newbarams.ajaja.module.remind.application.port.in.UpdateRemindInfoUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UpdateRemindInfoController {
	private final UpdateRemindInfoUseCase updateRemindInfoUseCase;

	@Authorization
	@PutMapping("/plans/{id}/reminds")
	@ResponseStatus(OK)
	public AjajaResponse<Void> modifyRemindInfo(
		@PathVariable Long id,
		@RequestBody PlanRequest.UpdateRemind request
	) {
		Long userId = SecurityUtil.getId();
		updateRemindInfoUseCase.update(userId, id, request);
		return AjajaResponse.ok();
	}
}
