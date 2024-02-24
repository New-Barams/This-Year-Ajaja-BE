package me.ajaja.module.auth.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import java.util.Arrays;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.jwt.JwtGenerator;
import me.ajaja.module.auth.dto.AuthResponse;

@RestController
@RequiredArgsConstructor
class MockLoginController {
	private final JwtGenerator jwtGenerator;
	private final Environment environment;

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
