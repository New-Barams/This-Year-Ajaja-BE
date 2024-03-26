package me.ajaja.module.remind.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.annotation.Login;
import me.ajaja.module.plan.dto.PlanRequest;
import me.ajaja.module.remind.application.port.in.UpdateRemindInfoUseCase;

@RestController
@RequiredArgsConstructor
public class UpdateRemindInfoController {
	private final UpdateRemindInfoUseCase updateRemindInfoUseCase;

	@Authorize
	@PutMapping("/plans/{id}/reminds")
	@ResponseStatus(OK)
	public AjajaResponse<Void> modifyRemindInfo(
		@Login Long userId,
		@PathVariable Long id,
		@RequestBody PlanRequest.UpdateRemind request
	) {
		updateRemindInfoUseCase.update(userId, id, request);
		return AjajaResponse.ok();
	}
}
