package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.user.application.port.in.WithdrawUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "user", description = "사용자 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class WithdrawController {
	private final WithdrawUseCase withdrawUseCase;

	@Operation(summary = "[토큰 필요] 회원 탈퇴 API", description = "인가 코드를 통해서 회원 탈퇴를 진행합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 회원 탈퇴를 하였습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
	})
	@DeleteMapping
	@ResponseStatus(OK)
	public AjajaResponse<Void> withdraw(@UserId Long id) {
		withdrawUseCase.withdraw(id);
		return AjajaResponse.ok();
	}
}
