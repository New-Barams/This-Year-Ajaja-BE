package me.ajaja.module.footprint.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.footprint.application.port.in.CreateFootprintUseCase;
import me.ajaja.module.footprint.dto.FootprintParam;
import me.ajaja.module.footprint.dto.FootprintRequest;

@RestController
@RequiredArgsConstructor
public class CreateFootprintController {
	private final CreateFootprintUseCase createFootprintUseCase;

	@Authorization
	@PostMapping("/targets/{id}/footprints")
	@ResponseStatus(CREATED)
	public AjajaResponse<Void> createFootprint(
		@PathVariable(name = "id") Long targetId,
		@RequestBody FootprintRequest.Create request
	) {
		Long userId = SecurityUtil.getUserId();
		FootprintParam.Create param = request.getParam();
		List<String> tags = request.getTags();

		createFootprintUseCase.create(userId, targetId, param, tags);
		return AjajaResponse.ok();
	}
}
