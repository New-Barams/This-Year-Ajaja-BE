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
import me.ajaja.module.plan.application.port.in.SwitchPlanStatusUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plans/{id}")
class SwitchPlanStatusController {
	private final SwitchPlanStatusUseCase switchPlanStatusUseCase;

	@Authorization
	@PutMapping("/public")
	@ResponseStatus(OK)
	public AjajaResponse<Void> switchPublicStatus(@PathVariable Long id) {
		Long userId = SecurityUtil.getUserId();
		switchPlanStatusUseCase.switchPublic(id, userId);

		return AjajaResponse.ok();
	}

	@Authorization
	@PutMapping("/remindable")
	@ResponseStatus(OK)
	public AjajaResponse<Void> switchRemindStatus(@PathVariable Long id) {
		Long userId = SecurityUtil.getUserId();
		switchPlanStatusUseCase.switchRemindable(id, userId);

		return AjajaResponse.ok();
	}

	@Authorization
	@PutMapping("/ajaja")
	@ResponseStatus(OK)
	public AjajaResponse<Void> switchAjajaStatus(@PathVariable Long id) {
		Long userId = SecurityUtil.getUserId();
		switchPlanStatusUseCase.switchAjajable(id, userId);

		return AjajaResponse.ok();
	}
}
