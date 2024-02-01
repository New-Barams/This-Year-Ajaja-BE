package me.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.user.application.port.in.RefreshNicknameUseCase;

@RestController
@RequiredArgsConstructor
class RefreshNicknameController {
	private final RefreshNicknameUseCase refreshNicknameUseCase;

	@Authorization
	@PostMapping("/users/refresh")
	@ResponseStatus(OK)
	public AjajaResponse<Void> refreshNickname() {
		Long id = SecurityUtil.getId();
		refreshNicknameUseCase.refresh(id);
		return AjajaResponse.ok();
	}
}
