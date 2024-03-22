package me.ajaja.module.footprint.domain;

import lombok.Getter;

@Getter
public class FreeFootprint extends Footprint {
	private FreeContent content;

	public FreeFootprint(Long id, Target target, Writer writer, Type type, Integer iconNumber, Title title,
		boolean visible, boolean deleted, FreeContent content
	) {
		super(id, target, writer, type, iconNumber, title, visible, deleted);
		this.content = content;
	}

	public FreeFootprint(Target target, Writer writer, Type type, Integer iconNumber, Title title, boolean visible,
		FreeContent content
	) {
		super(target, writer, type, iconNumber, title, visible);
		this.content = content;
	}
}
