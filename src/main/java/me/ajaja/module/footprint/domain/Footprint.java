package me.ajaja.module.footprint.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
@AllArgsConstructor
public abstract class Footprint extends SelfValidating<Footprint> {
	private final Long id;
	private final Target target;
	private final Writer writer;

	private Title title;
	private boolean visible;
	private boolean deleted;
}
