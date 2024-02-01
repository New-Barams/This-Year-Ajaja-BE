package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.security.annotation.Authorization;
import com.newbarams.ajaja.global.util.SecurityUtil;
import com.newbarams.ajaja.module.user.application.port.in.SendVerificationEmailUseCase;
import com.newbarams.ajaja.module.user.dto.UserRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class SendVerificationController {
	private final SendVerificationEmailUseCase sendVerificationEmailUseCase;

	@Authorization
	@PostMapping("/users/send-verification")
	@ResponseStatus(NO_CONTENT)
	public void sendVerification(@Valid @RequestBody UserRequest.EmailVerification request) {
		Long id = SecurityUtil.getId();
		sendVerificationEmailUseCase.sendVerification(id, request.getEmail());
	}
}
