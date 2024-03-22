package me.ajaja.module.footprint.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class FreeContent extends SelfValidating<FreeContent> {
	@NotBlank
	private final String content;

	public FreeContent(String content) {
		this.content = content;
		validateSelf();
	}
}
