package com.newbarams.ajaja.module.auth.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.module.auth.application.port.in.ReissueTokenUseCase;
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
class ReissueController {
	private final ReissueTokenUseCase reissueTokenUseCase;

	@Operation(summary = "토큰 재발급 API", description = "기존에 발급받은 토큰으로 새로운 토큰을 재발급할 수 있습니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 토큰을 재발급하였습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "로그인 이력이 존재하지 않습니다.")
	})
	@PostMapping("/reissue")
	@ResponseStatus(OK)
	public AjajaResponse<AuthResponse.Token> reissue(@Valid @RequestBody AuthRequest.Reissue request) {
		AuthResponse.Token response = reissueTokenUseCase.reissue(request.getAccessToken(), request.getRefreshToken());
		return AjajaResponse.ok(response);
	}
}
