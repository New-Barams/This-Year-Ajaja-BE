package me.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.annotation.Login;
import me.ajaja.module.user.application.port.in.SendVerificationEmailUseCase;
import me.ajaja.module.user.dto.UserRequest;

@RestController
@RequiredArgsConstructor
class SendVerificationController {
	private final SendVerificationEmailUseCase sendVerificationEmailUseCase;

	@Authorize
	@PostMapping("/users/send-verification")
	@ResponseStatus(NO_CONTENT)
	public void sendVerification(@Login Long id, @Valid @RequestBody UserRequest.EmailVerification request) {
		sendVerificationEmailUseCase.sendVerification(id, request.getEmail());
	}
}
