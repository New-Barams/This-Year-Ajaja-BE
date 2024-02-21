package me.ajaja.module.ajaja.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Target extends SelfValidating<Target> {
	@NotNull
	private final Long targetId;
	@NotBlank
	private final String title;

	public Target(Long targetId, String title) {
		this.targetId = targetId;
		this.title = title;
		validateSelf();
	}
}
