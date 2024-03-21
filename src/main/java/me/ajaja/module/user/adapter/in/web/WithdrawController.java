package me.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.annotation.Login;
import me.ajaja.module.user.application.port.in.WithdrawUseCase;

@RestController
@RequiredArgsConstructor
class WithdrawController {
	private final WithdrawUseCase withdrawUseCase;

	@Authorize
	@DeleteMapping("/users")
	@ResponseStatus(OK)
	public AjajaResponse<Void> withdraw(@Login Long id) {
		withdrawUseCase.withdraw(id);
		return AjajaResponse.ok();
	}
}
