package me.ajaja.module.footprint.domain;

import lombok.Getter;

@Getter
public class AjajaFootprint extends Footprint {
	private AjajaContent content;

	public AjajaFootprint(Long id, Target target, Writer writer, Type type, Integer iconNumber, Title title,
		boolean visible, boolean deleted, AjajaContent content) {
		super(id, target, writer, type, iconNumber, title, visible, deleted);
		this.content = content;
	}

	public AjajaFootprint(Target target, Writer writer, Type type, Integer iconNumber, Title title, boolean visible,
		AjajaContent content) {
		super(target, writer, type, iconNumber, title, visible);
		this.content = content;
	}
}
