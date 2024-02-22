package me.ajaja.module.auth.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.module.auth.application.port.in.LoginUseCase;
import me.ajaja.module.auth.dto.AuthRequest;
import me.ajaja.module.auth.dto.AuthResponse;

@RestController
@RequiredArgsConstructor
class LoginController {
	private final LoginUseCase loginUseCase;

	@PostMapping("/login")
	@ResponseStatus(OK)
	public AjajaResponse<AuthResponse.Token> login(@Valid @RequestBody AuthRequest.Login request) {
		AuthResponse.Token response = loginUseCase.login(request.getAuthorizationCode(), request.getRedirectUri());
		return AjajaResponse.ok(response);
	}
}
