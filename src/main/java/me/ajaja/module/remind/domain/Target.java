package me.ajaja.module.remind.domain;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

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
