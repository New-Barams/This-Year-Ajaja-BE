package me.ajaja.module.footprint.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;
import me.ajaja.module.footprint.dto.FootprintParam;

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
		this.id = null;
		this.target = target;
		this.writer = writer;
		this.type = type;
		this.title = title;
		this.visible = visible;
		this.deleted = false;
	}

	public abstract void setItems(FootprintParam.Create param);
}
