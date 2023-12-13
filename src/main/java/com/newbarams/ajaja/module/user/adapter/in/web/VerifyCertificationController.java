package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.user.application.port.in.VerifyCertificationUseCase;
import com.newbarams.ajaja.module.user.dto.UserRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "user", description = "사용자 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class VerifyCertificationController {
	private final VerifyCertificationUseCase verifyCertificationUseCase;

	@Operation(summary = "[토큰 필요] 인증 번호 검증 API", description = "발송된 인증 번호를 검증합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 이메일 인증을 완료하였습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
		@ApiResponse(responseCode = "409", description = "인증 번호가 일치하지 않습니다."),
	})
	@PostMapping("/verify")
	@ResponseStatus(OK)
	public AjajaResponse<Void> verifyCertification(
		@UserId Long id,
		@Valid @RequestBody UserRequest.Certification request
	) {
		verifyCertificationUseCase.verify(id, request.getCertification());
		return AjajaResponse.ok();
	}
}
