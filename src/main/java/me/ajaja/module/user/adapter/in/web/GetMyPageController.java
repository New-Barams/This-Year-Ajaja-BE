package me.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.user.application.port.out.GetMyPageQuery;
import me.ajaja.module.user.dto.UserResponse;

@RestController
@RequiredArgsConstructor
class GetMyPageController {
	private final GetMyPageQuery getMyPageQuery;

	@Authorization
	@GetMapping("/users")
	@ResponseStatus(OK)
	public AjajaResponse<UserResponse.MyPage> getMyPage() {
		Long id = SecurityUtil.getId();
		UserResponse.MyPage response = getMyPageQuery.findUserInfoById(id);
		return AjajaResponse.ok(response);
	}
}
