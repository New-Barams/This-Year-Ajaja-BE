package me.ajaja.module.footprint.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorize;
import me.ajaja.global.security.annotation.Login;
import me.ajaja.module.footprint.application.port.in.CreateFootprintUseCase;
import me.ajaja.module.footprint.dto.FootprintRequest;

@RestController
@RequiredArgsConstructor
public class CreateFootprintController {
	private final CreateFootprintUseCase createFootprintUseCase;

	@Authorize
	@PostMapping("/footprints")
	@ResponseStatus(CREATED)
	public AjajaResponse<Void> createFootprint(@Login Long userId, @RequestBody FootprintRequest.Create request) {
		createFootprintUseCase.create(userId, request);
		return AjajaResponse.ok();
	}
}
