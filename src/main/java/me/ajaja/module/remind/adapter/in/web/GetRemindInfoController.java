package me.ajaja.module.remind.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.remind.application.port.out.FindPlanRemindQuery;
import me.ajaja.module.remind.dto.RemindResponse;

@RestController
@RequiredArgsConstructor
public class GetRemindInfoController {
	private final FindPlanRemindQuery findPlanRemindQuery;

	@Authorization
	@GetMapping("/reminds/{planId}")
	@ResponseStatus(HttpStatus.OK)
	public AjajaResponse<RemindResponse.RemindInfo> getRemindResponse(@PathVariable Long planId) {
		Long userId = SecurityUtil.getUserId();
		RemindResponse.RemindInfo response = findPlanRemindQuery.findByUserIdAndPlanId(userId, planId);
		return AjajaResponse.ok(response);
	}
}
