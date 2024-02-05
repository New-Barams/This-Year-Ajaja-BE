package me.ajaja.module.plan.adapter.in.web;

import static org.springframework.http.HttpStatus.*;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import me.ajaja.global.common.AjajaResponse;
import me.ajaja.global.security.annotation.Authorization;
import me.ajaja.global.util.SecurityUtil;
import me.ajaja.module.ajaja.application.SwitchAjajaService;

@RestController
@RequiredArgsConstructor
public class SwitchAjajaController {
	private final SwitchAjajaService switchAjajaService;

	@Authorization
	@PostMapping("/plans/{id}/ajaja")
	@ResponseStatus(OK)
	public AjajaResponse<Void> switchAjaja(@PathVariable Long id) {
		Long userId = SecurityUtil.getUserId();
		switchAjajaService.switchOrAddIfNotExist(userId, id);

		return AjajaResponse.ok();
	}
}
