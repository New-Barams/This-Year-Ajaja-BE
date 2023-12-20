package com.newbarams.ajaja.module.auth.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.module.auth.application.port.in.LoginUseCase;
import com.newbarams.ajaja.module.auth.dto.AuthRequest;
import com.newbarams.ajaja.module.auth.dto.AuthResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequiredArgsConstructor
class LoginController {
	private final LoginUseCase loginUseCase;

	@Operation(summary = "로그인 API", description = "인증 서버로부터 발급 받은 인가 코드로 로그인을 시도합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 로그인하였습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 인가 코드입니다."),
	})
	@PostMapping("/login")
	@ResponseStatus(OK)
	public AjajaResponse<AuthResponse.Token> login(@Valid @RequestBody AuthRequest.Login request) {
		AuthResponse.Token response = loginUseCase.login(request.getAuthorizationCode(), request.getRedirectUri());
		return AjajaResponse.ok(response);
	}
}
