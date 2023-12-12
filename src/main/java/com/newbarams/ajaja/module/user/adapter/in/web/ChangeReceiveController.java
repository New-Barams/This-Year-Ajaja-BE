package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.user.application.port.in.ChangeReceiveTypeUseCase;
import com.newbarams.ajaja.module.user.dto.UserRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "user", description = "사용자 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ChangeReceiveController {
	private final ChangeReceiveTypeUseCase changeReceiveTypeUseCase;

	@Operation(summary = "[토큰 필요] 수신 종류 변경 API", description = "리마인드를 수신 방법을 변경합니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 수신 방법이 변경되었습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다."),
		@ApiResponse(responseCode = "404", description = "사용자가 존재하지 않습니다."),
	})
	@PutMapping("/receive")
	@ResponseStatus(OK)
	public AjajaResponse<Void> changeReceiveType(@UserId Long id, @RequestBody UserRequest.Receive request) {
		changeReceiveTypeUseCase.change(id, request.getType());
		return AjajaResponse.ok();
	}
}
