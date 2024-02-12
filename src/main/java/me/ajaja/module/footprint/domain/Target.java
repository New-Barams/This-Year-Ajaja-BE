package me.ajaja.module.footprint.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Target extends SelfValidating<Target> {
	@NotNull
	private final Long id;

	@NotBlank
	@Size(max = 20)
	private final String title;

	@ConstructorProperties({"id", "title"})
	public Target(Long id, String title) {
		this.id = id;
		this.title = title;
		this.validateSelf();
	}
}
