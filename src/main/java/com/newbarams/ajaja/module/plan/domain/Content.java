package com.newbarams.ajaja.module.plan.domain;

import com.newbarams.ajaja.global.common.SelfValidating;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends SelfValidating<Content> {
	@NotBlank
	@Size(max = 50)
	private String title;

	@NotBlank
	@Size(max = 300)
	private String description;

	public Content(String title, String description) {
		this.title = title;
		this.description = description;
		this.validateSelf();
	}
}
