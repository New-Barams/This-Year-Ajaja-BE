package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.security.annotation.UserId;
import com.newbarams.ajaja.module.user.application.port.in.LogoutUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class LogoutController {
	private final LogoutUseCase logoutUseCase;

	@PostMapping("/users/logout")
	@ResponseStatus(NO_CONTENT)
	public void logout(@UserId Long id) {
		logoutUseCase.logout(id);
	}
}
