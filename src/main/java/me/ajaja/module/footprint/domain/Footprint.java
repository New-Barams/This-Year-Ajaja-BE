package me.ajaja.module.footprint.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ajaja.global.common.SelfValidating;

@Getter
@AllArgsConstructor
public abstract class Footprint extends SelfValidating<Footprint> {
	private final Target target;
	private final Writer writer;

	private Title title;
	private boolean visible;
	private boolean deleted;

	private Set<Tag> tags = new HashSet<>();
	private List<Ajaja> ajajas = new ArrayList<>();
}
