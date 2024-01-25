package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.user.application.port.in.RefreshNicknameUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class RefreshNicknameController {
	private final RefreshNicknameUseCase refreshNicknameUseCase;

	@PostMapping("/users/refresh")
	@ResponseStatus(OK)
	public AjajaResponse<Void> refreshNickname(@UserId Long id) {
		refreshNicknameUseCase.refresh(id);
		return AjajaResponse.ok();
	}
}
