package com.newbarams.ajaja.module.remind.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.annotation.UserId;
import com.newbarams.ajaja.module.remind.application.port.out.FindPlanRemindQuery;
import com.newbarams.ajaja.module.remind.dto.RemindResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class GetRemindInfoController {
	private final FindPlanRemindQuery findPlanRemindQuery;

	@GetMapping("/reminds/{planId}")
	@ResponseStatus(HttpStatus.OK)
	public AjajaResponse<RemindResponse.RemindInfo> getRemindResponse(
		@UserId Long userId,
		@PathVariable Long planId
	) {
		RemindResponse.RemindInfo response = findPlanRemindQuery.findByUserIdAndPlanId(userId, planId);
		return AjajaResponse.ok(response);
	}
}
