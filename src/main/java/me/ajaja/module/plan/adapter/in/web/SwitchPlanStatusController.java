package me.ajaja.module.plan.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.annotation.Login;
import me.ajaja.module.plan.application.port.in.SwitchPlanStatusUseCase;

@RestController
@RequiredArgsConstructor
@RequestMapping("/plans/{id}")
class SwitchPlanStatusController {
	private final SwitchPlanStatusUseCase switchPlanStatusUseCase;

	@Authorize
	@PutMapping("/public")
	@ResponseStatus(OK)
	public AjajaResponse<Void> switchPublicStatus(@Login Long userId, @PathVariable Long id) {
		switchPlanStatusUseCase.switchPublic(id, userId);
		return AjajaResponse.ok();
	}

	@Authorize
	@PutMapping("/remindable")
	@ResponseStatus(OK)
	public AjajaResponse<Void> switchRemindStatus(@Login Long userId, @PathVariable Long id) {
		switchPlanStatusUseCase.switchRemindable(id, userId);
		return AjajaResponse.ok();
	}

	@Authorize
	@PutMapping("/ajajable")
	@ResponseStatus(OK)
	public AjajaResponse<Void> switchAjajaStatus(@Login Long userId, @PathVariable Long id) {
		switchPlanStatusUseCase.switchAjajable(id, userId);
		return AjajaResponse.ok();
	}
}
