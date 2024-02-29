package me.ajaja.module.footprint.domain;

import java.beans.ConstructorProperties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
public class Target extends SelfValidating<Target> {
	@NotNull
	private final Long id;
	private final String title;

	@ConstructorProperties({"id", "title"})
	public Target(Long id, String title) {
		this.id = id;
		this.title = title;
		this.validateSelf();
	}

	public static Target init(Long id) {
		return new Target(id, null);
	}
}
