package me.ajaja.module.footprint.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Tag extends SelfValidating<Tag> {
	@NotNull
	private final Long id;

	@NotBlank
	@Size(max = 10)
	private final String name;

	@ConstructorProperties({"id", "name"})
	public Tag(Long id, String name) {
		this.id = id;
		this.name = name;
		this.validateSelf();
	}
}
