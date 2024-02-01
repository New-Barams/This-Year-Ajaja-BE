package me.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.user.application.port.in.SendVerificationEmailUseCase;
import me.ajaja.module.user.dto.UserRequest;

@RestController
@RequiredArgsConstructor
class SendVerificationController {
	private final SendVerificationEmailUseCase sendVerificationEmailUseCase;

	@Authorization
	@PostMapping("/users/send-verification")
	@ResponseStatus(NO_CONTENT)
	public void sendVerification(@Valid @RequestBody UserRequest.EmailVerification request) {
		Long id = SecurityUtil.getUserId();
		sendVerificationEmailUseCase.sendVerification(id, request.getEmail());
	}
}
