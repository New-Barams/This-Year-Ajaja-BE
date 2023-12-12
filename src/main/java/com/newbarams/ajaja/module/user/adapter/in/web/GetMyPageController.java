package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.user.domain.UserQueryRepository;
import com.newbarams.ajaja.module.user.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "user", description = "사용자 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
class GetMyPageController {
	private final UserQueryRepository userQueryRepository;

	@Operation(summary = "[토큰 필요] 마이페이지 API", description = "사용자의 정보를 불러옵니다.", responses = {
		@ApiResponse(responseCode = "200", description = "성공적으로 정보를 불러왔습니다."),
		@ApiResponse(responseCode = "400", description = "유효하지 않은 토큰입니다. <br> 상세 정보는 응답을 확인 바랍니다.")
	})
	@GetMapping
	@ResponseStatus(OK)
	public AjajaResponse<UserResponse.MyPage> getMyPage(@UserId Long id) {
		UserResponse.MyPage response = userQueryRepository.findUserInfoById(id);
		return AjajaResponse.ok(response);
	}
}
