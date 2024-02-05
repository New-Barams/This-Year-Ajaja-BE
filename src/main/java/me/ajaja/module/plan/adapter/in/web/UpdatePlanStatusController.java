package me.ajaja.module.plan.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.plan.application.port.in.UpdatePlanStatusUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plans/{id}")
class UpdatePlanStatusController {
	private final UpdatePlanStatusUseCase updatePlanStatusUseCase;

	@Authorization
	@PutMapping("/public")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updatePlanPublicStatus(@PathVariable Long id) {
		Long userId = SecurityUtil.getUserId();
		updatePlanStatusUseCase.updatePublicStatus(id, userId);

		return AjajaResponse.ok();
	}

	@Authorization
	@PutMapping("/remindable")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updatePlanRemindStatus(@PathVariable Long id) {
		Long userId = SecurityUtil.getUserId();
		updatePlanStatusUseCase.updateRemindStatus(id, userId);

		return AjajaResponse.ok();
	}

	@Authorization
	@PutMapping("/ajaja")
	@ResponseStatus(OK)
	public AjajaResponse<Void> updatePlanAjajaStatus(@PathVariable Long id) {
		Long userId = SecurityUtil.getUserId();
		updatePlanStatusUseCase.updateAjajaStatus(id, userId);

		return AjajaResponse.ok();
	}
}
