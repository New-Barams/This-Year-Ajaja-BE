package com.newbarams.ajaja.module.user.presentation;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.module.user.application.LoginService;
import com.newbarams.ajaja.module.user.application.ReissueTokenService;
import com.newbarams.ajaja.module.user.dto.UserRequest;
import com.newbarams.ajaja.module.user.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "auth", description = "인증 API")
@RestController
@RequiredArgsConstructor
public class AuthController {
	private final LoginService loginService;
	private final ReissueTokenService reissueTokenService;

	@Operation(summary = "로그인 API", description = "인증 서버로부터 발급 받은 인가 코드로 로그인을 시도합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 로그인하였습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 인가 코드입니다."),
	})
	@PostMapping("/login")
	@ResponseStatus(OK)
	public AjajaResponse<UserResponse.Token> login(@RequestParam("code") String authorizationCode) {
		UserResponse.Token response = loginService.login(authorizationCode);
		return AjajaResponse.ok(response);
	}

	@Operation(summary = "토큰 재발급 API", description = "기존에 발급받은 토큰으로 새로운 토큰을 재발급할 수 있습니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 토큰을 재발급하였습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "로그인 이력이 존재하지 않습니다.")
	})
	@PostMapping("/reissue")
	@ResponseStatus(OK)
	public AjajaResponse<UserResponse.Token> reissue(@Valid @RequestBody UserRequest.Reissue request) {
		UserResponse.Token response = reissueTokenService.reissue(request.accessToken(), request.refreshToken());
		return AjajaResponse.ok(response);
	}
}
