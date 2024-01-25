package com.newbarams.ajaja.module.remind.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.remind.application.port.in.SendTestRemindUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SendTestRemindController {
	private final SendTestRemindUseCase sendTestRemindUseCase;

	@PostMapping("/reminds/test")
	@ResponseStatus(HttpStatus.OK)
	public AjajaResponse<String> sendTestRemind(@UserId Long userId) {
		String remindType = sendTestRemindUseCase.send(userId);
		return AjajaResponse.ok(remindType);
	}
}
