package com.newbarams.ajaja.module.ajaja.presentation;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.newbarams.ajaja.global.common.AjajaResponse;
import com.newbarams.ajaja.global.security.common.UserId;
import com.newbarams.ajaja.module.ajaja.application.SwitchAjajaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class AjajaController {
	private final SwitchAjajaService switchAjajaService;

	@PostMapping("/{id}/ajaja")
	public AjajaResponse switchAjaja(@UserId Long userId, @PathVariable Long id) {
		switchAjajaService.switchOrAddIfNotExist(userId, id);

		return new AjajaResponse(true, null);
	}
}
