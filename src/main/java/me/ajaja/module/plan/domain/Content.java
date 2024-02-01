package me.ajaja.module.plan.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.ajaja.global.common.SelfValidating;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends SelfValidating<Content> {
	@NotBlank
	@Size(max = 20)
	private String title;

	@NotBlank
	@Size(max = 250)
	private String description;

	public Content(String title, String description) {
		this.title = title;
		this.description = description;
		this.validateSelf();
	}
}
