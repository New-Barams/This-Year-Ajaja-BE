package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.annotation.Authorization;
import com.newbarams.ajaja.global.util.SecurityUtil;
import com.newbarams.ajaja.module.user.application.port.in.ChangeRemindTypeUseCase;
import com.newbarams.ajaja.module.user.dto.UserRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class ChangeRemindTypeController {
	private final ChangeRemindTypeUseCase changeRemindTypeUseCase;

	@Authorization
	@PutMapping("/users/receive")
	@ResponseStatus(OK)
	public AjajaResponse<Void> changeRemindType(@RequestBody UserRequest.Receive request) {
		Long id = SecurityUtil.getId();
		changeRemindTypeUseCase.change(id, request.getType());
		return AjajaResponse.ok();
	}
}
