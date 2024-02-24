package me.ajaja.module.footprint.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FreeFootprint extends Footprint {
	@NotBlank
	private String content;

	public FreeFootprint(Long id, Target target, Writer writer, Type type, Title title, boolean visible,
		boolean deleted, String content) {
		super(id, target, writer, type, title, visible, deleted);
		this.content = content;
		this.validateSelf();
	}

	public FreeFootprint(Target target, Writer writer, Type type, Title title, boolean visible, String content) {
		super(target, writer, type, title, visible);
		this.content = content;
		this.validateSelf();
	}
}
