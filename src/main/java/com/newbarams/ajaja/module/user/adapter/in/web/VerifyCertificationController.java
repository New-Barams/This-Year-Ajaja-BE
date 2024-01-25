package com.newbarams.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.user.application.port.in.VerifyCertificationUseCase;
import com.newbarams.ajaja.module.user.dto.UserRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class VerifyCertificationController {
	private final VerifyCertificationUseCase verifyCertificationUseCase;

	@PostMapping("/users/verify")
	@ResponseStatus(OK)
	public AjajaResponse<Void> verifyCertification(
		@UserId Long id,
		@Valid @RequestBody UserRequest.Certification request
	) {
		verifyCertificationUseCase.verify(id, request.getCertification());
		return AjajaResponse.ok();
	}
}
