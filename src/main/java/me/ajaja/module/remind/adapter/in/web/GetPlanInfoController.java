package me.ajaja.module.remind.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.annotation.Login;
import me.ajaja.module.plan.dto.PlanResponse;
import me.ajaja.module.remind.application.port.in.GetTargetInfoUseCase;

@RestController
@RequiredArgsConstructor
public class GetPlanInfoController {
	private final GetTargetInfoUseCase getTargetInfoUseCase;

	@Authorize
	@GetMapping("/plans/main")
	@ResponseStatus(OK)
	public AjajaResponse<List<PlanResponse.MainInfo>> getPlanInfo(@Login Long userId) {
		List<PlanResponse.MainInfo> response = getTargetInfoUseCase.load(userId);
		return AjajaResponse.ok(response);
	}
}
