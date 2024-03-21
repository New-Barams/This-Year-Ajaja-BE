package me.ajaja.module.remind.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.annotation.Login;
import me.ajaja.module.remind.application.port.out.FindTargetRemindQuery;
import me.ajaja.module.remind.dto.RemindResponse;

@RestController
@RequiredArgsConstructor
public class GetRemindInfoController {
	private final FindTargetRemindQuery findTargetRemindQuery;

	@Authorize
	@GetMapping("/reminds/{planId}")
	@ResponseStatus(HttpStatus.OK)
	public AjajaResponse<RemindResponse.RemindInfo> getRemindResponse(@Login Long userId, @PathVariable Long planId) {
		RemindResponse.RemindInfo response = findTargetRemindQuery.findByUserIdAndPlanId(userId, planId);
		return AjajaResponse.ok(response);
	}
}
