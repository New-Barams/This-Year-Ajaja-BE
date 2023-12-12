package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.user.application.port.in.RenewNicknameUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "user", description = "사용자 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class RenewNicknameController {
	private final RenewNicknameUseCase renewNicknameUseCase;

	@Operation(summary = "[토큰 필요] 닉네임 새로고침 API", description = "새로운 랜덤 닉네임을 생성합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 새로운 닉네임으로 변경했습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다.")
	})
	@PostMapping("/refresh")
	@ResponseStatus(OK)
	public AjajaResponse<String> renewNickname(@UserId Long id) {
		String newNickname = renewNicknameUseCase.renew(id);
		return AjajaResponse.ok(newNickname);
	}
}
