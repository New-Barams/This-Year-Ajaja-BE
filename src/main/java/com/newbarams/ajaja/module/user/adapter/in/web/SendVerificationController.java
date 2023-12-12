package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.user.application.port.in.SendVerificationEmailUseCase;
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
class SendVerificationController {
	private final SendVerificationEmailUseCase sendVerificationEmailUseCase;

	@Operation(summary = "[토큰 필요] 이메일 검증 요청 API", description = "리마인드 받을 이메일을 검증하기 위해 인증을 요청합니다.", responses = {
		@ApiResponse(responseCode = "204", description = "성공적으로 인증 메일을 전송했습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
		@ApiResponse(responseCode = "409", description = "이메일 인증을 할 수 없습니다. 기존 리마인드 이메일과 다른 이메일을 입력해야 합니다.")
	})
	@PostMapping("/send-verification")
	@ResponseStatus(NO_CONTENT)
	public void sendVerification(
		@UserId Long id,
		@Valid @RequestBody UserRequest.EmailVerification request
	) {
		sendVerificationEmailUseCase.sendVerification(id, request.getEmail());
	}
}
