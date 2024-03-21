package me.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.annotation.Login;
import me.ajaja.module.user.application.port.out.GetMyPageQuery;
import me.ajaja.module.user.dto.UserResponse;

@RestController
@RequiredArgsConstructor
class GetMyPageController {
	private final GetMyPageQuery getMyPageQuery;

	@Authorize
	@GetMapping("/users")
	@ResponseStatus(OK)
	public AjajaResponse<UserResponse.MyPage> getMyPage(@Login Long id) {
		UserResponse.MyPage response = getMyPageQuery.findUserInfoById(id);
		return AjajaResponse.ok(response);
	}
}
