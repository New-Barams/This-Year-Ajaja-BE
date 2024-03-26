package me.ajaja.module.user.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.annotation.Login;
import me.ajaja.module.user.application.port.in.VerifyCertificationUseCase;
import me.ajaja.module.user.dto.UserRequest;

@RestController
@RequiredArgsConstructor
class VerifyCertificationController {
	private final VerifyCertificationUseCase verifyCertificationUseCase;

	@Authorize
	@PostMapping("/users/verify")
	@ResponseStatus(OK)
	public AjajaResponse<Void> verifyCertification(
		@Login Long id,
		@Valid @RequestBody UserRequest.Certification request
	) {
		verifyCertificationUseCase.verify(id, request.getCertification());
		return AjajaResponse.ok();
	}
}
