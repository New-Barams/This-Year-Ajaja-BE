package com.newbarams.ajaja.module.remind.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class Target extends SelfValidating<Target> {
	@NotNull
	private final Long id;
	private final String title;

	public Target(Long id, String title) {
		this.id = id;
		this.title = title;
		validateSelf();
	}
}
