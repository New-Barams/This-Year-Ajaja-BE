package com.newbarams.ajaja.module.remind.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Info extends SelfValidating<Info> {
	@NotBlank
	@Size(max = 255)
	private String content;

	public Info(String content) {
		this.content = content;
		this.validateSelf();
	}
}
