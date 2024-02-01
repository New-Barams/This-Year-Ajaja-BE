package me.ajaja.module.auth.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import java.util.Arrays;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.jwt.JwtGenerator;
import me.ajaja.module.auth.application.port.in.LoginUseCase;
import me.ajaja.module.auth.dto.AuthRequest;
import me.ajaja.module.auth.dto.AuthResponse;

@RestController
@RequiredArgsConstructor
class LoginController {
	private final LoginUseCase loginUseCase;
	private final JwtGenerator jwtGenerator;
	private final Environment environment;

	@PostMapping("/login")
	@ResponseStatus(OK)
	public AjajaResponse<AuthResponse.Token> login(@Valid @RequestBody AuthRequest.Login request) {
		AuthResponse.Token response = loginUseCase.login(request.getAuthorizationCode(), request.getRedirectUri());
		return AjajaResponse.ok(response);
	}

	@PostMapping("/mock/login")
	@ResponseStatus(OK)
	public AjajaResponse<AuthResponse.Token> login() {
		throwIfNotLocal();
		AuthResponse.Token response = jwtGenerator.login(1L);
		return AjajaResponse.ok(response);
	}

	private void throwIfNotLocal() {
		if (!Arrays.asList(environment.getActiveProfiles()).contains("local")) {
			throw new UnsupportedOperationException();
		}
	}
}
