package me.ajaja.module.plan.adapter.in.web;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.jwt.JwtParser;
import me.ajaja.global.util.BearerUtils;
import me.ajaja.module.plan.application.port.in.LoadPlanDetailUseCase;
import me.ajaja.module.plan.dto.PlanResponse;

@RestController
@RequiredArgsConstructor
class GetPlanController {
	private final LoadPlanDetailUseCase loadPlanDetailUseCase;
	private final JwtParser jwtParser; // todo: delete when authentication filtering update

	@GetMapping("/plans/{id}")
	@ResponseStatus(OK)
	public AjajaResponse<PlanResponse.Detail> getPlanWithOptionalUser(
		@RequestHeader(value = AUTHORIZATION, required = false) String accessToken,
		@PathVariable Long id
	) {
		Long userId = parseUserId(accessToken);
		PlanResponse.Detail response = loadPlanDetailUseCase.loadByIdAndOptionalUser(userId, id);
		return AjajaResponse.ok(response);
	}

	private Long parseUserId(String accessToken) {
		if (accessToken == null) {
			return null;
		}

		BearerUtils.validate(accessToken);
		return jwtParser.parseId(BearerUtils.resolve(accessToken));
	}
}
