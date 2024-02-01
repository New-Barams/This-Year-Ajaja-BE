package me.ajaja.module.remind.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.remind.application.port.in.SendTestRemindUseCase;

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
