package me.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.user.application.port.in.ChangeRemindTypeUseCase;
import me.ajaja.module.user.dto.UserRequest;

@RestController
@RequiredArgsConstructor
class ChangeRemindTypeController {
	private final ChangeRemindTypeUseCase changeRemindTypeUseCase;

	@Authorize
	@PutMapping("/users/receive")
	@ResponseStatus(OK)
	public AjajaResponse<Void> changeRemindType(@RequestBody UserRequest.Receive request) {
		Long id = SecurityUtil.getUserId();
		changeRemindTypeUseCase.change(id, request.getType());
		return AjajaResponse.ok();
	}
}
