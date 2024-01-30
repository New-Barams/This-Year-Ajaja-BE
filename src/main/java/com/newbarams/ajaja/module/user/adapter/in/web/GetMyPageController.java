package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.annotation.Authorization;
import com.newbarams.ajaja.global.security.annotation.UserId;
import com.newbarams.ajaja.module.user.application.port.out.GetMyPageQuery;
import com.newbarams.ajaja.module.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class GetMyPageController {
	private final GetMyPageQuery getMyPageQuery;

	@Authorization
	@GetMapping("/users")
	@ResponseStatus(OK)
	public AjajaResponse<UserResponse.MyPage> getMyPage(@UserId Long id) {
		UserResponse.MyPage response = getMyPageQuery.findUserInfoById(id);
		return AjajaResponse.ok(response);
	}
}
