package me.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.user.application.port.in.LogoutUseCase;

@RestController
@RequiredArgsConstructor
class LogoutController {
	private final LogoutUseCase logoutUseCase;

	@Authorization
	@PostMapping("/users/logout")
	@ResponseStatus(NO_CONTENT)
	public void logout() {
		Long id = SecurityUtil.getUserId();
		logoutUseCase.logout(id);
	}
}
