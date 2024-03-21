package me.ajaja.module.remind.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.remind.application.port.in.SendTrialRemindUseCase;

@RestController
@RequiredArgsConstructor
public class SendTrialRemindController {
	private final SendTrialRemindUseCase sendTrialRemindUseCase;

	@Authorize
	@PostMapping("/reminds/test")
	@ResponseStatus(OK)
	public AjajaResponse<String> sendTrialRemind() {
		Long userId = SecurityUtil.getUserId();
		String remindType = sendTrialRemindUseCase.send(userId);
		return AjajaResponse.ok(remindType);
	}
}
