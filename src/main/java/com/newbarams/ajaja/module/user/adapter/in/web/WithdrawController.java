package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.annotation.UserId;
import com.newbarams.ajaja.module.user.application.port.in.WithdrawUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class WithdrawController {
	private final WithdrawUseCase withdrawUseCase;

	@DeleteMapping("/users")
	@ResponseStatus(OK)
	public AjajaResponse<Void> withdraw(@UserId Long id) {
		withdrawUseCase.withdraw(id);
		return AjajaResponse.ok();
	}
}
