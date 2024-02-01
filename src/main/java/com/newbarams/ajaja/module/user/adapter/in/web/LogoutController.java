package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.security.annotation.Authorization;
import com.newbarams.ajaja.global.util.SecurityUtil;
import com.newbarams.ajaja.module.user.application.port.in.LogoutUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class LogoutController {
	private final LogoutUseCase logoutUseCase;

	@Authorization
	@PostMapping("/users/logout")
	@ResponseStatus(NO_CONTENT)
	public void logout() {
		Long id = SecurityUtil.getId();
		logoutUseCase.logout(id);
	}
}
