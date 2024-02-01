package me.ajaja.module.auth.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.module.auth.application.port.in.ReissueTokenUseCase;
import me.ajaja.module.auth.dto.AuthRequest;
import me.ajaja.module.auth.dto.AuthResponse;

@RestController
@RequiredArgsConstructor
class ReissueController {
	private final ReissueTokenUseCase reissueTokenUseCase;

	@PostMapping("/reissue")
	@ResponseStatus(OK)
	public AjajaResponse<AuthResponse.Token> reissue(@Valid @RequestBody AuthRequest.Reissue request) {
		AuthResponse.Token response = reissueTokenUseCase.reissue(request.getAccessToken(), request.getRefreshToken());
		return AjajaResponse.ok(response);
	}
}
