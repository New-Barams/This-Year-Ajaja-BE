package com.newbarams.ajaja.module.remind.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.annotation.Authorization;
import com.newbarams.ajaja.global.util.SecurityUtil;
import com.newbarams.ajaja.module.remind.application.port.in.SendTestRemindUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SendTestRemindController {
	private final SendTestRemindUseCase sendTestRemindUseCase;

	@Authorization
	@PostMapping("/reminds/test")
	@ResponseStatus(OK)
	public AjajaResponse<String> sendTestRemind() {
		Long userId = SecurityUtil.getId();
		String remindType = sendTestRemindUseCase.send(userId);
		return AjajaResponse.ok(remindType);
	}
}
