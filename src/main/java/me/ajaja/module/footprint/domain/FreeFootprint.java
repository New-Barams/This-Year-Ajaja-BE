package me.ajaja.module.footprint.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class FreeFootprint extends Footprint {
	@NotBlank
	private String content;

	public FreeFootprint(Long id, Target target, Writer writer, Title title, boolean visible, boolean deleted,
		String content) {
		super(id, target, writer, title, visible, deleted);
		this.content = content;
		this.validateSelf();
	}
}
