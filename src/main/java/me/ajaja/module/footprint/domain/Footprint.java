package me.ajaja.module.footprint.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
@AllArgsConstructor
public abstract class Footprint extends SelfValidating<Footprint> {
	public enum Type {
		FREE, KPT
	}

	private final Long id;
	private final Target target;
	private final Writer writer;
	private final Type type;

	private Title title;
	private boolean visible;
	private boolean deleted;

	public Footprint(Target target, Writer writer, Type type, Title title, boolean visible) {
		this(null, target, writer, type, title, visible, false);
	}
}
