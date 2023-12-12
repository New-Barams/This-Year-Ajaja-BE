package com.newbarams.ajaja.module.remind.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class Info extends SelfValidating<Info> {
	@NotBlank
	@Size(max = 255)
	private final String content;

	public Info(String content) {
		this.content = content;
		this.validateSelf();
	}
}
